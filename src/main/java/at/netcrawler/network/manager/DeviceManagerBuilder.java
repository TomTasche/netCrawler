package at.netcrawler.network.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.connection.Connection;
import at.netcrawler.network.connection.ConnectionType;
import at.netcrawler.network.manager.cli.CommandLineDeviceManagerFactory;
import at.netcrawler.network.manager.snmp.SNMPDeviceManagerFactory;
import at.netcrawler.network.model.NetworkDevice;


// TODO: default constructor?
public class DeviceManagerBuilder {
	
	private Map<ConnectionType, DeviceManagerFactory> managerFactoryMap = new HashMap<ConnectionType, DeviceManagerFactory>();
	
	public DeviceManagerBuilder() {
		addDeviceManagerFactory(ConnectionType.TELNET,
				new CommandLineDeviceManagerFactory());
		addDeviceManagerFactory(ConnectionType.SSH,
				new CommandLineDeviceManagerFactory());
		addDeviceManagerFactory(ConnectionType.SNMP,
				new SNMPDeviceManagerFactory());
	}
	
	public void addDeviceManagerFactory(ConnectionType connectionType,
			DeviceManagerFactory deviceManagerFactory) {
		managerFactoryMap.put(connectionType, deviceManagerFactory);
	}
	
	public void removeDeviceManagerFactory(ConnectionType connectionType) {
		managerFactoryMap.remove(connectionType);
	}
	
	@SuppressWarnings("unchecked")
	public <M extends DeviceManager> M buildDeviceManager(NetworkDevice device,
			Connection connection) throws IOException {
		DeviceManagerFactory managerFactory = managerFactoryMap.get(connection
				.getConnectionType());
		if (managerFactory == null)
			throw new IllegalArgumentException("Unsupported connection type");
		
		return (M) managerFactory.buildDeviceManager(device, connection);
	}
	
}