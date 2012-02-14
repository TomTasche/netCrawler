package at.netcrawler.network.connection.ssh;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public class LocalSSHGateway extends SSHGateway {
	
	@Override
	protected SSHConnection openConnectionGenericImpl(IPDeviceAccessor accessor,
			SSHSettings settings) throws IOException {
		return new LocalSSHConnection(accessor, settings);
	}
	
}