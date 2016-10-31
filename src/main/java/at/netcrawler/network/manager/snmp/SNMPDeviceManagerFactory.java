package at.netcrawler.network.manager.snmp;

import java.io.IOException;

import at.netcrawler.network.connection.snmp.SNMPConnection;
import at.netcrawler.network.manager.GenericDeviceManagerFactory;
import at.netcrawler.network.model.NetworkDevice;


public class SNMPDeviceManagerFactory extends
		GenericDeviceManagerFactory<SNMPDeviceManager, SNMPConnection> {
	
	@Override
	public SNMPDeviceManager buildDeviceManagerGeneric(NetworkDevice device,
			SNMPConnection connection) throws IOException {
		return new SNMPDeviceManager(device, connection);
	}
	
}