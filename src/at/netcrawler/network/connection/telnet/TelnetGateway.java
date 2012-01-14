package at.netcrawler.network.connection.telnet;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.TCPIPConnectionGateway;
import at.netcrawler.network.connection.TCPIPDeviceConnection;


public abstract class TelnetGateway extends
		TCPIPConnectionGateway<TelnetSettings> {
	
	@Override
	public abstract TCPIPDeviceConnection openConnection(
			IPDeviceAccessor accessor, TelnetSettings settings) throws IOException;
	
}