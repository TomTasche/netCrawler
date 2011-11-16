package at.netcrawler.network.connection.ssh;

import java.io.IOException;

import at.netcrawler.network.IPDeviceAccessor;
import at.netcrawler.network.connection.IPConnectionGateway;


public abstract class SSHConnectionGateway extends
		IPConnectionGateway<SSHConnectionSettings> {
	
	@Override
	public Class<SSHConnectionSettings> getConnectionSettingsClass() {
		return SSHConnectionSettings.class;
	}
	
	@Override
	public abstract SSHConnection openConnection(IPDeviceAccessor accessor,
			SSHConnectionSettings settings) throws IOException;
	
}