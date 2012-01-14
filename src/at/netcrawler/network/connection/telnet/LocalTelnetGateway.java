package at.netcrawler.network.connection.telnet;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.TCPIPDeviceConnection;


public class LocalTelnetGateway extends TelnetGateway {
	
	@Override
	public TCPIPDeviceConnection openConnection(IPDeviceAccessor accessor,
			TelnetSettings settings) throws IOException {
		return new LocalTelnetConnection(accessor, settings);
	}
	
}