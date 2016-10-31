package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.DeviceAccessor;


public abstract class AbstractConnection implements Connection {
	
	private boolean closed;
	
	public AbstractConnection(DeviceAccessor accessor,
			ConnectionSettings settings) {}
	
	public abstract ConnectionType getConnectionType();
	
	public final boolean isClosed() {
		return closed;
	}
	
	public final void close() throws IOException {
		closed = true;
		
		closeImpl();
	}
	
	protected abstract void closeImpl() throws IOException;
	
}