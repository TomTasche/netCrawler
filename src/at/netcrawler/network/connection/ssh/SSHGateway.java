package at.netcrawler.network.connection.ssh;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.TCPIPConnectionGateway;


public abstract class SSHGateway extends TCPIPConnectionGateway<SSHSettings> {
	
	@Override
	public final Class<SSHConnection> getConnectionClass() {
		return SSHConnection.class;
	}
	
	@Override
	public final Class<SSHSettings> getSettingsClass() {
		return SSHSettings.class;
	}
	
	@Override
	protected abstract SSHConnection openConnectionGenericImpl(
			IPDeviceAccessor accessor, SSHSettings settings) throws IOException;
	
}