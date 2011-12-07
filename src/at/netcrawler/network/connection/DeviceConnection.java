package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.DeviceAccessor;


public abstract class DeviceConnection {
	
	private boolean connected;
	private boolean closed;
	
	public final boolean isConnected() {
		return connected;
	}
	
	public final boolean isClosed() {
		return closed;
	}
	
	public abstract Class<? extends DeviceAccessor> getAccessorClass();
	
	public abstract Class<? extends ConnectionSettings> getSettingsClass();
	
	public abstract DeviceAccessor getAccessor();
	
	public abstract ConnectionSettings getSettings();
	
	public final void connect(DeviceAccessor accessor,
			ConnectionSettings settings) throws IOException {
		if (connected) throw new IllegalStateException("Already connected!");
		
		connectImpl(accessor, settings);
		
		connected = true;
	}
	
	protected abstract void connectImpl(DeviceAccessor accessor,
			ConnectionSettings settings) throws IOException;
	
	public final void close() throws IOException {
		connected = false;
		closed = true;
		
		closeImpl();
	}
	
	protected abstract void closeImpl() throws IOException;
	
}