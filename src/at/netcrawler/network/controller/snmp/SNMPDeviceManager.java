package at.netcrawler.network.controller.snmp;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.andiwand.library.network.mac.MACAddress;
import at.netcrawler.network.Capability;
import at.netcrawler.network.connection.snmp.SNMPConnection;
import at.netcrawler.network.connection.snmp.SNMPObject;
import at.netcrawler.network.connection.snmp.SNMPObject.Type;
import at.netcrawler.network.controller.DeviceManager;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.model.extension.EthernetInterfaceExtension;


public class SNMPDeviceManager extends DeviceManager<SNMPConnection> {
	
	public SNMPDeviceManager(NetworkDevice device, SNMPConnection connection)
			throws IOException {
		super(device, connection);
	}
	
	
	@Override
	public String getIdentication() throws IOException {
		return connection.get("1.3.6.1.2.1.1.2.0").getValue();
	}
	
	@Override
	public String getHostname() throws IOException {
		return connection.get("1.3.6.1.2.1.1.5.0").getValue();
	}
	
	@Override
	public String getSystem() throws IOException {
		return connection.get("1.3.6.1.2.1.1.1.0").getValue();
	}
	
	@Override
	public Set<Capability> getCapabilities() throws IOException {
		String services = connection.get("1.3.6.1.2.1.1.7.0").getValue();
		int servicesInt = Integer.parseInt(services);
		
		Set<Capability> result = new HashSet<Capability>();
		if ((servicesInt & 0x04) != 0)
			result.add(Capability.ROUTER);
		return result;
	}
	
	@Override
	public Set<NetworkInterface> getInterfaces() throws IOException {
		List<SNMPObject[]> table = connection.walkBulkTable(
				"1.3.6.1.2.1.31.1.1.1.1",	// name
				"1.3.6.1.2.1.2.2.1.2",		// description
				"1.3.6.1.2.1.2.2.1.6"		// address
		);
		
		Set<NetworkInterface> interfaces = new HashSet<NetworkInterface>();
		
		for (int i = 0; i < table.size(); i++) {
			SNMPObject[] row = table.get(i);
			NetworkInterface newInterface = new NetworkInterface();
			
			newInterface.setValue(NetworkInterface.NAME, row[0].getValue());
			newInterface.setValue(NetworkInterface.FULL_NAME, row[1].getValue());
			String addressString = row[2].getValue();
			if (!addressString.isEmpty()) {
				try {
					MACAddress address = MACAddress.getByAddress(addressString);
					
					newInterface.addExtension(EthernetInterfaceExtension.class);
					newInterface.setValue(EthernetInterfaceExtension.ADDRESS,
							address);
				} catch (Exception e) {
					
				}
			}
			
			interfaces.add(newInterface);
		}
		
		return interfaces;
	}
	
	
	@Override
	public boolean setHostname(String hostname) throws IOException {
		return connection.setAndVerify("1.3.6.1.2.1.1.5.0", Type.STRING, hostname);
	}
	
}