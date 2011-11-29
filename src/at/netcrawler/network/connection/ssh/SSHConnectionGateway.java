package at.netcrawler.network.connection.ssh;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.IPConnectionGateway;


public abstract class SSHConnectionGateway<S extends SSHConnectionSettings>
		extends IPConnectionGateway<S> {
	
	@Override
	public abstract SSHConnection openConnection(IPDeviceAccessor accessor,
			S settings) throws IOException;
	
}