package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public abstract class TCPIPConnectionGateway<S extends TCPIPConnectionSettings> extends
		IPConnectionGateway<S> {
	
	@Override
	public abstract Class<? extends TCPIPDeviceConnection> getConnectionClass();
	
	@Override
	public abstract TCPIPDeviceConnection openConnectionGenericImpl(
			IPDeviceAccessor accessor, S settings) throws IOException;
	
}