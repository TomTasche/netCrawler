package at.netcrawler.network.connection.telnet;

import java.io.IOException;

import at.netcrawler.network.IPDeviceAccessor;
import at.netcrawler.network.connection.DeviceConnection;


public class LocalTelnetConnectionGateway extends TelnetConnectionGateway {
	
	@Override
	public Class<? extends DeviceConnection> getDeviceConnectionClass() {
		return LocalTelnetConnection.class;
	}
	
	@Override
	public LocalTelnetConnection openConnection(IPDeviceAccessor accessor,
			TelnetConnectionSettings settings) throws IOException {
		return new LocalTelnetConnection(accessor, settings);
	}
	
}