package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.DeviceAccessor;


public abstract class ConnectionGateway<A extends DeviceAccessor, S extends ConnectionSettings> {
	
	public abstract Class<? extends DeviceConnection> getDeviceConnectionClass();
	
	public abstract Class<A> getDeviceAccessorClass();
	
	public abstract Class<S> getConnectionSettingsClass();
	
	public abstract DeviceConnection openConnection(A accessor, S settings)
			throws IOException;
	
}