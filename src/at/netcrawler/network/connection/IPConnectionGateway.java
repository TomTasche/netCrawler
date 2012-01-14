package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public abstract class IPConnectionGateway<S extends ConnectionSettings> extends
		ConnectionGateway<IPDeviceAccessor, S> {
	
	public abstract IPDeviceConnection openConnection(
			IPDeviceAccessor accessor, S settings) throws IOException;
	
}