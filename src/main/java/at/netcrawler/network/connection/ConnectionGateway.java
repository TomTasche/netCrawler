package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.DeviceAccessor;


public abstract class ConnectionGateway {
	
	public abstract ConnectionType getConnectionType();
	
	public abstract Class<? extends Connection> getConnectionClass();
	
	public abstract Class<? extends DeviceAccessor> getAccessorClass();
	
	public abstract Class<? extends ConnectionSettings> getSettingsClass();
	
	public final Connection openConnection(DeviceAccessor accessor,
			ConnectionSettings settings) throws IOException {
		if (!accessor.getClass().equals(getAccessorClass()))
			throw new IllegalArgumentException("Illegal accessor class!");
		if (!settings.getClass().equals(getSettingsClass()))
			throw new IllegalArgumentException("Illegal settings class!");
		
		return openConnectionImpl(accessor, settings);
	}
	
	protected abstract Connection openConnectionImpl(DeviceAccessor accessor,
			ConnectionSettings settings) throws IOException;
	
}