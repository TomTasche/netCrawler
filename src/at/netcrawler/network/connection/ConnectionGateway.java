package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.DeviceAccessor;


public abstract class ConnectionGateway<A extends DeviceAccessor, S extends ConnectionSettings> {
	
	public abstract DeviceConnection openConnection(A accessor, S settings) throws IOException;
	
}