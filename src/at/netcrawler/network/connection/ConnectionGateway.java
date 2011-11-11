package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.DeviceAccessor;


public abstract class ConnectionGateway<DA extends DeviceAccessor, CS extends ConnectionSettings> {
	
	public abstract Class<? extends DeviceConnection> getDeviceConnectionClass();
	public abstract Class<DA> getDeviceAccessorClass();
	public abstract Class<CS> getConnectionSettingsClass();
	
	public abstract DeviceConnection openConnection(DA accessor, CS settings)
			throws IOException;
	
}