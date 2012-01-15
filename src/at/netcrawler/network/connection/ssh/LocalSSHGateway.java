package at.netcrawler.network.connection.ssh;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public abstract class LocalSSHGateway extends SSHGateway {
	
	@Override
	public SSHConnection openConnectionGenericImpl(IPDeviceAccessor accessor,
			SSHSettings settings) throws IOException {
		return new LocalSSHConnection(accessor, settings);
	}
	
}