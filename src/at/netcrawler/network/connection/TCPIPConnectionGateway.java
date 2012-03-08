package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public abstract class TCPIPConnectionGateway<C extends TCPIPDeviceConnection, S extends TCPIPConnectionSettings> extends
		IPConnectionGateway<C, S> {
	
	@Override
	protected abstract C openConnectionGenericImpl(IPDeviceAccessor accessor,
			S settings) throws IOException;
	
}