package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.DeviceAccessor;


public interface Connection {
	
	public ConnectionType getConnectionType();
	
	public DeviceAccessor getDeviceAccessor();
	
	public ConnectionSettings getConnectionSettings();
	
	public boolean isClosed();
	
	public void close() throws IOException;
	
}