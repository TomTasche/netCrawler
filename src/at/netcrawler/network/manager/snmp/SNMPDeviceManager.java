package at.netcrawler.network.manager.snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.andiwand.library.network.ip.IPAddress;
import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.network.ip.SubnetMask;
import at.andiwand.library.network.mac.MACAddress;
import at.andiwand.library.util.QuickPattern;
import at.netcrawler.network.Capability;
import at.netcrawler.network.connection.snmp.SNMPConnection;
import at.netcrawler.network.connection.snmp.SNMPObject;
import at.netcrawler.network.connection.snmp.SNMPObject.Type;
import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.model.extension.EthernetInterfaceExtension;
import at.netcrawler.network.model.extension.IPInterfaceExtension;


public class SNMPDeviceManager extends DeviceManager {
	
	private static final String IDENTICATION_OID = "1.3.6.1.2.1.1.2.0";
	private static final String HOSTNAME_OID = "1.3.6.1.2.1.1.5.0";
	private static final String SYSTEM_OID = "1.3.6.1.2.1.1.1.0";
	private static final String CAPABILITIES_OID = "1.3.6.1.2.1.1.7.0";
	private static final String UPTIME_OID = "1.3.6.1.2.1.1.3.0";
	private static final QuickPattern UPTIME_PATTERN = new QuickPattern(
			"(\\d+):(\\d+):(\\d+)\\.(\\d+)");
	private static final String INTERFACES_NAMES_OID = "1.3.6.1.2.1.31.1.1.1.1";
	private static final String INTERFACES_DESCRIPTIONS_OID = "1.3.6.1.2.1.2.2.1.2";
	private static final String INTERFACES_PHYSICAL_ADDRESSES_OID = "1.3.6.1.2.1.2.2.1.6";
	private static final String INTERFACES_IP_IFID_OID = "1.3.6.1.2.1.4.20.1.2";
	private static final String INTERFACES_IP_ADDRESSES_OID = "1.3.6.1.2.1.4.20.1.1";
	private static final String INTERFACES_IP_NETMASK_OID = "1.3.6.1.2.1.4.20.1.3";
	private static final String MANAGEMENT_ADDRESSES_OID = "1.3.6.1.2.1.4.20.1.1";
	
	private final SNMPConnection connection;
	
	public SNMPDeviceManager(NetworkDevice device, SNMPConnection connection)
			throws IOException {
		super(device);
		
		this.connection = connection;
	}
	
	@Override
	public String getIdentication() throws IOException {
		return connection.get(IDENTICATION_OID).getValue();
	}
	
	@Override
	public String getHostname() throws IOException {
		return connection.get(SYSTEM_OID).getValue();
	}
	
	@Override
	public String getSystem() throws IOException {
		return connection.get(SYSTEM_OID).getValue();
	}
	
	@Override
	public Set<Capability> getCapabilities() throws IOException {
		String services = connection.get(CAPABILITIES_OID).getValue();
		// TODO: fix
		if (services.equals("noSuchInstance")) return null;
		int servicesInt = Integer.parseInt(services);
		
		Set<Capability> result = new HashSet<Capability>();
		if ((servicesInt & 0x04) != 0) result.add(Capability.ROUTER);
		return result;
	}
	
	@Override
	public Capability getMajorCapability() throws IOException {
		// TODO implement
		return null;
	}
	
	@Override
	public long getUptime() throws IOException {
		String timeticks = connection.get(UPTIME_OID).getValue();
		String[] time = UPTIME_PATTERN.findGroups(timeticks);
		long hours = Long.parseLong(time[0]);
		long minutes = Long.parseLong(time[1]);
		long seconds = Long.parseLong(time[2]);
		long millis = Long.parseLong(time[3]);
		return hours * 60 * 60 * 1000 + minutes * 60 * 1000 + seconds * 1000
				+ millis;
	}
	
	@Override
	public Set<NetworkInterface> getInterfaces() throws IOException {
		List<SNMPObject[]> table = connection.walkTable(INTERFACES_NAMES_OID,
				INTERFACES_DESCRIPTIONS_OID, INTERFACES_PHYSICAL_ADDRESSES_OID);
		List<SNMPObject[]> ipTable = connection.walkTable(
				INTERFACES_IP_IFID_OID, INTERFACES_IP_ADDRESSES_OID,
				INTERFACES_IP_NETMASK_OID);
		
		List<NetworkInterface> interfaces = new ArrayList<NetworkInterface>();
		
		for (int i = 0; i < table.size(); i++) {
			SNMPObject[] row = table.get(i);
			NetworkInterface interfaze = new NetworkInterface();
			
			interfaze.setValue(NetworkInterface.NAME, row[0].getValue());
			interfaze.setValue(NetworkInterface.FULL_NAME, row[1].getValue());
			String addressString = row[2].getValue();
			if (!addressString.isEmpty()) {
				try {
					MACAddress address = MACAddress.getByAddress(addressString);
					
					interfaze.addExtension(EthernetInterfaceExtension.class);
					interfaze.setValue(EthernetInterfaceExtension.ADDRESS,
							address);
				} catch (Exception e) {}
			}
			
			interfaces.add(interfaze);
		}
		
		for (SNMPObject[] row : ipTable) {
			int id = Integer.parseInt(row[0].getValue()) - 1;
			IPAddress address = IPv4Address.getByAddress(row[1].getValue());
			SubnetMask netmask = new SubnetMask(row[2].getValue());
			
			NetworkInterface interfaze = interfaces.get(id);
			interfaze.addExtension(IPInterfaceExtension.class);
			interfaze.setValue(IPInterfaceExtension.ADDRESS, address);
			interfaze.setValue(IPInterfaceExtension.NETMASK, netmask);
		}
		
		return new HashSet<NetworkInterface>(interfaces);
	}
	
	@Override
	public Set<IPAddress> getManagementAddresses() throws IOException {
		List<SNMPObject> addresses = connection.walk(MANAGEMENT_ADDRESSES_OID);
		Set<IPAddress> result = new HashSet<IPAddress>();
		
		// TODO: support ipv6
		for (SNMPObject address : addresses) {
			result.add(IPv4Address.getByAddress(address.getValue()));
		}
		
		result.remove(IPv4Address.LOCALHOST);
		
		return result;
	}
	
	@Override
	public boolean setHostname(String hostname) throws IOException {
		return connection.setAndVerify(HOSTNAME_OID, Type.STRING, hostname);
	}
	
	@Override
	public Set<IPAddress> discoverNeighbors() {
		// TODO implement
		return null;
	}
	
}