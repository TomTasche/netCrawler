package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.DeviceAccessor;


public abstract class Connection {
	
	private boolean closed;
	
	public Connection(DeviceAccessor accessor, ConnectionSettings settings) {}
	
	public final boolean isClosed() {
		return closed;
	}
	
	public final void close() throws IOException {
		closed = true;
		
		closeImpl();
	}
	
	protected abstract void closeImpl() throws IOException;
	
}