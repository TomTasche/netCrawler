package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public abstract class TCPIPConnectionGateway<S extends TCPIPConnectionSettings> extends
		IPConnectionGateway<S> {
	
	public abstract TCPIPDeviceConnection openConnection(
			IPDeviceAccessor accessor, S settings) throws IOException;
	
}