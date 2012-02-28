package at.netcrawler.network.manager.snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.andiwand.library.network.ip.IPAddress;
import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.network.ip.SubnetMask;
import at.andiwand.library.network.mac.MACAddress;
import at.andiwand.library.util.ObjectIdentifier;
import at.andiwand.library.util.Timeticks;
import at.netcrawler.DeviceSystem;
import at.netcrawler.network.Capability;
import at.netcrawler.network.InterfaceType;
import at.netcrawler.network.connection.snmp.SNMPConnection;
import at.netcrawler.network.connection.snmp.SNMPEntry;
import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.model.extension.EthernetInterfaceExtension;
import at.netcrawler.network.model.extension.IPInterfaceExtension;


public class SNMPDeviceManager extends DeviceManager {
	
	private static enum SupportedInterfaceType {
		
		ETHERNET_CSMACD(6, InterfaceType.ETHERNET),
		SOFTWARE_LOOPBACK(24, InterfaceType.LOOPBACK),
		ETHERNET_3MBIT(26, InterfaceType.ETHERNET),
		FRAME_RELAY(32, InterfaceType.FRAME_RELAY);
		
		private static final Map<Integer, InterfaceType> TYPE_MAP = new HashMap<Integer, InterfaceType>();
		
		static {
			for (SupportedInterfaceType type : values()) {
				TYPE_MAP.put(type.snmpType, type.type);
			}
		}
		
		public static InterfaceType getType(int snmpType) {
			InterfaceType type = TYPE_MAP.get(snmpType);
			if (type == null) type = InterfaceType.UNKNOWN;
			return type;
		}
		
		private final int snmpType;
		private final InterfaceType type;
		
		private SupportedInterfaceType(int snmpType, InterfaceType type) {
			this.snmpType = snmpType;
			this.type = type;
		}
		
	}
	
	private static final ObjectIdentifier IDENTICATION_OID = new ObjectIdentifier(
			"1.3.6.1.6.3.10.2.1.1.0");
	private static final ObjectIdentifier HOSTNAME_OID = new ObjectIdentifier(
			"1.3.6.1.2.1.1.5.0");
	private static final ObjectIdentifier SYSTEM_OID = new ObjectIdentifier(
			"1.3.6.1.2.1.1.1.0");
	private static final ObjectIdentifier CAPABILITIES_OID = new ObjectIdentifier(
			"1.3.6.1.2.1.1.7.0");
	private static final ObjectIdentifier UPTIME_OID = new ObjectIdentifier(
			"1.3.6.1.2.1.1.3.0");
	private static final ObjectIdentifier INTERFACES_NAMES_OID = new ObjectIdentifier(
			"1.3.6.1.2.1.31.1.1.1.1");
	private static final ObjectIdentifier INTERFACES_DESCRIPTIONS_OID = new ObjectIdentifier(
			"1.3.6.1.2.1.2.2.1.2");
	private static final ObjectIdentifier INTERFACES_TYPE_OID = new ObjectIdentifier(
			"1.3.6.1.2.1.2.2.1.3");
	private static final ObjectIdentifier INTERFACES_PHYSICAL_ADDRESSES_OID = new ObjectIdentifier(
			"1.3.6.1.2.1.2.2.1.6");
	private static final ObjectIdentifier INTERFACES_IP_IFID_OID = new ObjectIdentifier(
			"1.3.6.1.2.1.4.20.1.2");
	private static final ObjectIdentifier INTERFACES_IP_ADDRESSES_OID = new ObjectIdentifier(
			"1.3.6.1.2.1.4.20.1.1");
	private static final ObjectIdentifier INTERFACES_IP_NETMASK_OID = new ObjectIdentifier(
			"1.3.6.1.2.1.4.20.1.3");
	private static final ObjectIdentifier MANAGEMENT_ADDRESSES_OID = new ObjectIdentifier(
			"1.3.6.1.2.1.4.20.1.1");
	
	private final SNMPConnection connection;
	
	public SNMPDeviceManager(NetworkDevice device, SNMPConnection connection)
			throws IOException {
		super(device);
		
		this.connection = connection;
	}
	
	@Override
	protected String getIdentication() throws IOException {
		return connection.get(IDENTICATION_OID).getValue();
	}
	
	@Override
	protected String getHostname() throws IOException {
		return connection.get(SYSTEM_OID).getValue();
	}
	
	// TODO: implement
	@Override
	protected DeviceSystem getSystem() throws IOException {
		return null;
	}
	
	@Override
	protected String getSystemString() throws IOException {
		return connection.get(SYSTEM_OID).getValue();
	}
	
	@Override
	protected Set<Capability> getCapabilities() throws IOException {
		Integer servicesInteger = connection.get(CAPABILITIES_OID).getValue();
		if (servicesInteger == null) return null;
		int services = servicesInteger;
		
		Set<Capability> result = new HashSet<Capability>();
		if ((services & 0x04) != 0) result.add(Capability.ROUTER);
		return result;
	}
	
	@Override
	protected Capability getMajorCapability() throws IOException {
		// TODO implement
		return null;
	}
	
	@Override
	protected long getUptime() throws IOException {
		Timeticks timeticks = connection.get(UPTIME_OID).getValue();
		return timeticks.getMillis();
	}
	
	@Override
	protected Set<NetworkInterface> getInterfaces() throws IOException {
		List<SNMPEntry[]> table = connection.walkTable(INTERFACES_NAMES_OID,
				INTERFACES_DESCRIPTIONS_OID, INTERFACES_TYPE_OID,
				INTERFACES_PHYSICAL_ADDRESSES_OID);
		List<SNMPEntry[]> ipTable = connection.walkTable(
				INTERFACES_IP_IFID_OID, INTERFACES_IP_ADDRESSES_OID,
				INTERFACES_IP_NETMASK_OID);
		
		List<NetworkInterface> interfaces = new ArrayList<NetworkInterface>();
		
		for (int i = 0; i < table.size(); i++) {
			SNMPEntry[] row = table.get(i);
			NetworkInterface interfaze = new NetworkInterface();
			
			interfaze.setValue(NetworkInterface.NAME, row[0].getValue());
			interfaze.setValue(NetworkInterface.FULL_NAME, row[1].getValue());
			InterfaceType interfaceType = SupportedInterfaceType
					.getType((Integer) row[2].getValue());
			interfaze.setValue(NetworkInterface.TYPE, interfaceType);
			String addressString = row[3].getValue();
			if (!addressString.isEmpty()) {
				try {
					MACAddress address = new MACAddress(addressString);
					
					interfaze
							.addExtension(EthernetInterfaceExtension.EXTENSION);
					interfaze.setValue(EthernetInterfaceExtension.ADDRESS,
							address);
				} catch (Exception e) {}
			}
			
			interfaces.add(interfaze);
		}
		
		for (SNMPEntry[] row : ipTable) {
			int id = (Integer) row[0].getValue() - 1;
			IPAddress address = row[1].getValue();
			SubnetMask netmask = new SubnetMask((IPv4Address) row[2].getValue());
			
			NetworkInterface interfaze = interfaces.get(id);
			interfaze.addExtension(IPInterfaceExtension.EXTENSION);
			interfaze.setValue(IPInterfaceExtension.ADDRESS, address);
			interfaze.setValue(IPInterfaceExtension.NETMASK, netmask);
		}
		
		return new HashSet<NetworkInterface>(interfaces);
	}
	
	@Override
	protected Set<IPAddress> getManagementAddresses() throws IOException {
		List<SNMPEntry> addresses = connection.walk(MANAGEMENT_ADDRESSES_OID);
		Set<IPAddress> result = new HashSet<IPAddress>();
		
		// TODO: support ipv6
		for (SNMPEntry address : addresses) {
			result.add((IPAddress) address.getValue());
		}
		
		result.remove(IPv4Address.LOCALHOST);
		
		return result;
	}
	
	@Override
	protected boolean setHostname(String hostname) throws IOException {
		return connection.setAndVerify(HOSTNAME_OID, hostname);
	}
	
	@Override
	public Map<IPv4Address, NetworkInterface> discoverNeighbors() {
		// TODO implement
		return null;
	}
	
}