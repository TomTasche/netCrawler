package at.netcrawler.network.connection.ssh.executor;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.SSHConnection;


public abstract class SSHExecutorConnection extends SSHConnection implements
		SSHExecutor {
	
	public SSHExecutorConnection(IPDeviceAccessor accessor,
			SSHExecutorConnectionSettings settings) {
		super(accessor, settings);
	}
	
}