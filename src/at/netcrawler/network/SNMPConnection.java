package at.netcrawler.network;

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
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;


public class SNMPConnection {
	
	private final Snmp snmp;
	private final CommunityTarget target;
	
	
	
	
	public SNMPConnection(InetAddress address, int port) throws IOException {
		this(GenericAddress.parse("udp:" + address.getHostAddress() + "/" + port));
	}
	public SNMPConnection(Address address) throws IOException {
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
	
	
	
	
	public PDU fetch(String oid) throws IOException {
		return fetch(new OID(oid));
	}
	
	public PDU fetch(OID oid) throws IOException {
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
	
	
	public List<PDU> fetchRange(String oid) throws IOException {
		return fetchRange(new OID(oid));
	}
	
	public List<PDU> fetchRange(final OID oid) throws IOException {
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
	
	
	
	public PDU set(String oid, String var) throws IOException {
		return set(new OID(oid), new OctetString(var));
	}
	public PDU set(OID oid, Variable var) throws IOException {
		PDU pdu = new PDU();
		pdu.add(new VariableBinding(oid, var));
		pdu.setType(PDU.SET);
		
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
	
}