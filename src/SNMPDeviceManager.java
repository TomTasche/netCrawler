import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import at.andiwand.library.network.mac.MACAddress;

public class SNMPDeviceManager {
	
	public static final int SYSTEM_SERVICES_PHYSICAL = 0x01;
	public static final int SYSTEM_SERVICES_DATALINK = 0x02;
	public static final int SYSTEM_SERVICES_INTERNET = 0x04;
	public static final int SYSTEM_SERVICES_END2END = 0x08;
	public static final int SYSTEM_SERVICES_APPLICATIONS = 0x40;
	
	public static final int IP_FORWARDING = 1;
	public static final int IP_NOT_FORWARDING = 2;
	
	
	
	
	private final Snmp snmp;
	private final CommunityTarget target;
	
	
	
	public SNMPDeviceManager(InetAddress address, int port) throws IOException {
		this(GenericAddress.parse("udp:" + address.getHostAddress() + "/" + port));
	}
	public SNMPDeviceManager(Address address) throws IOException {
		TransportMapping transport = new DefaultUdpTransportMapping();
		snmp = new Snmp(transport);
		USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3
				.createLocalEngineID()), 0);
		SecurityModels.getInstance().addSecurityModel(usm);
		transport.listen();
		
		target = new CommunityTarget();
		target.setCommunity(new OctetString("public"));
		target.setAddress(address);
		target.setRetries(2);
		target.setTimeout(1500);
		target.setVersion(SnmpConstants.version1);
	}
	
	
	
	private PDU fetch(String oid) throws IOException {
		return fetch(new OID(oid));
	}
	private PDU fetch(OID oid) throws IOException {
		PDU pdu = new PDU();
		pdu.add(new VariableBinding(oid));
		pdu.setType(PDU.GET);
		
		final Object monitor = new Object();
		final PDU[] response = new PDU[1];
		
		ResponseListener listener = new ResponseListener() {
			public void onResponse(ResponseEvent event) {
				((Snmp) event.getSource()).cancel(event.getRequest(), this);
				response[0] = event.getResponse();
				
				synchronized (monitor) {
					monitor.notify();
				}
			}
		};
		
		snmp.send(pdu, target, null, listener);
		
		synchronized (monitor) {
			try {
				monitor.wait();
			} catch (InterruptedException e) {
				return null;
			}
		}
		
		return response[0];
	}
	
	private List<PDU> fetchRange(String oid) throws IOException {
		return fetchRange(new OID(oid));
	}
	private List<PDU> fetchRange(final OID oid) throws IOException {
		final Object monitor = new Object();
		final OID[] lastOid = new OID[] {oid};
		final List<PDU> result = new LinkedList<PDU>();
		
		while (lastOid[0] != null) {
			PDU pdu = new PDU();
			pdu.add(new VariableBinding(lastOid[0]));
			pdu.setType(PDU.GETNEXT);
			
			ResponseListener listener = new ResponseListener() {
				public void onResponse(ResponseEvent event) {
					((Snmp) event.getSource()).cancel(event.getRequest(), this);
					PDU response = event.getResponse();
					OID responseOid = response.get(0).getOid();
					
					if (responseOid.equals(lastOid) || !responseOid.startsWith(oid)) {
						lastOid[0] = null;
					} else {
						result.add(response);
						lastOid[0] = responseOid;
					}
					
					synchronized (monitor) {
						monitor.notify();
					}
				}
			};
			
			snmp.send(pdu, target, null, listener);
			
			synchronized (monitor) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {
					return null;
				}
			}
		}
		
		return result;
	}
	
	
	public String fetchSystemDescription() throws IOException {
		PDU response = fetch("1.3.6.1.2.1.1.1.0");
		VariableBinding variableBinding = response.get(0);
		
		return variableBinding.getVariable().toString();
	}
	public String fetchSystemObjectId() throws IOException {
		PDU response = fetch("1.3.6.1.2.1.1.2.0");
		VariableBinding variableBinding = response.get(0);
		
		List<PDU> tmp = fetchRange(variableBinding.getVariable().toString());
		for (PDU pdu : tmp) {
			System.out.println(pdu);
		}
		
		return variableBinding.getVariable().toString();
	}
	public String fetchSystemUpTime() throws IOException {
		PDU response = fetch("1.3.6.1.2.1.1.3.0");
		VariableBinding variableBinding = response.get(0);
		
		return variableBinding.getVariable().toString();
	}
	public int fetchSystemServices() throws IOException {
		PDU response = fetch("1.3.6.1.2.1.1.7.0");
		VariableBinding variableBinding = response.get(0);
		
		return variableBinding.getVariable().toInt();
	}
	
	public List<Interface> fetchInterfaces() throws IOException {
		List<PDU> interfaceNumbers = fetchRange("1.3.6.1.2.1.2.2.1.1");
		List<PDU> interfaceDescriptions = fetchRange("1.3.6.1.2.1.2.2.1.2");
		List<PDU> interfaceMtus = fetchRange("1.3.6.1.2.1.2.2.1.4");
		List<PDU> interfaceAddresses = fetchRange("1.3.6.1.2.1.2.2.1.6");
		List<PDU> interfaceAdminState = fetchRange("1.3.6.1.2.1.2.2.1.7");
		List<PDU> interfaceNames = fetchRange("1.3.6.1.2.1.31.1.1.1.1");
		List<Interface> interfaces = new LinkedList<Interface>();
		VariableBinding variableBinding;
		
		for (int i = 0; i < interfaceNumbers.size(); i++) {
			Interface newInterface = new Interface();
			
			variableBinding = interfaceNumbers.get(i).get(0);
			newInterface.setNumber(variableBinding.getVariable().toInt());
			
			variableBinding = interfaceDescriptions.get(i).get(0);
			newInterface.setDescription(variableBinding.getVariable().toString());
			
			variableBinding = interfaceMtus.get(i).get(0);
			newInterface.setMtu(variableBinding.getVariable().toInt());
			
			variableBinding = interfaceAddresses.get(i).get(0);
			String macAddressString = variableBinding.getVariable().toString();
			if (!macAddressString.isEmpty()) {
				newInterface.setMacAddress(MACAddress.getByAddress(macAddressString));
			}
			
			variableBinding = interfaceAdminState.get(i).get(0);
			newInterface.setAdminState(variableBinding.getVariable().toInt());
			
			variableBinding = interfaceNames.get(i).get(0);
			newInterface.setName(variableBinding.getVariable().toString());
			
			interfaces.add(newInterface);
		}
		
		return interfaces;
	}
	
	public int fetchIpForwarding() throws IOException {
		PDU response = fetch("1.3.6.1.2.1.4.1.0");
		VariableBinding variableBinding = response.get(0);
		
		return variableBinding.getVariable().toInt();
	}
	
	public ARPTable fetchArpTable() throws IOException {
		List<PDU> macAddresses = fetchRange("1.3.6.1.2.1.4.22.1.2");
		List<PDU> inetAddresses = fetchRange("1.3.6.1.2.1.4.22.1.3");
		ARPTable arpTable = new ARPTable();
		VariableBinding variableBinding;
		
		for (int i = 0; i < macAddresses.size(); i++) {
			ARPTable.ARPEntry entry = new ARPTable.ARPEntry();
			
			variableBinding = macAddresses.get(i).get(0);
			entry.setMacAddress(MACAddress.getByAddress(variableBinding
					.getVariable().toString()));
			
			variableBinding = inetAddresses.get(i).get(0);
			entry.setInetAddress(InetAddress.getByName(variableBinding
					.getVariable().toString()));
			
			arpTable.addEntry(entry);
		}
		
		return arpTable;
	}
	
}