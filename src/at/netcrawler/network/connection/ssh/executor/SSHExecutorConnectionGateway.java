package at.netcrawler.network.connection.ssh.executor;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.SSHConnectionGateway;


public abstract class SSHExecutorConnectionGateway extends
		SSHConnectionGateway<SSHExecutorConnectionSettings> {
	
	@Override
	public Class<SSHExecutorConnectionSettings> getConnectionSettingsClass() {
		return SSHExecutorConnectionSettings.class;
	}
	
	@Override
	public abstract SSHExecutorConnection openConnection(
			IPDeviceAccessor accessor, SSHExecutorConnectionSettings settings)
			throws IOException;
	
}