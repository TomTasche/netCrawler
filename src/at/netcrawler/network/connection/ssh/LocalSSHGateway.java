package at.netcrawler.network.connection.ssh;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public class LocalSSHGateway extends SSHGateway<LocalSSHConnection> {
	
	@Override
	public Class<LocalSSHConnection> getConnectionClass() {
		return LocalSSHConnection.class;
	}
	
	@Override
	protected LocalSSHConnection openConnectionGenericImpl(
			IPDeviceAccessor accessor, SSHSettings settings) throws IOException {
		return new LocalSSHConnection(accessor, settings);
	}
	
}