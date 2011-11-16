package at.netcrawler.network.connection.snmp;

import java.io.IOException;

import at.netcrawler.network.IPDeviceAccessor;
import at.netcrawler.network.connection.DeviceConnection;


public class LocalSNMPConnectionGateway extends SNMPConnectionGateway {
	
	@Override
	public Class<? extends DeviceConnection> getDeviceConnectionClass() {
		return LocalSNMPConnection.class;
	}
	
	@Override
	public LocalSNMPConnection openConnection(IPDeviceAccessor accessor,
			SNMPConnectionSettings settings) throws IOException {
		return new LocalSNMPConnection(accessor, settings);
	}
	
}