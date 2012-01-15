package at.netcrawler.network.connection.telnet;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public class LocalTelnetGateway extends TelnetGateway {
	
	@Override
	public LocalTelnetConnection openConnectionGenericImpl(
			IPDeviceAccessor accessor, TelnetSettings settings)
			throws IOException {
		return new LocalTelnetConnection(accessor, settings);
	}
	
}