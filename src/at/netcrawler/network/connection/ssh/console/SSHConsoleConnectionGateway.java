package at.netcrawler.network.connection.ssh.console;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.IPConnectionGateway;


public abstract class SSHConsoleConnectionGateway extends
		IPConnectionGateway<SSHConsoleConnectionSettings> {
	
	@Override
	public Class<SSHConsoleConnectionSettings> getConnectionSettingsClass() {
		return SSHConsoleConnectionSettings.class;
	}
	
	@Override
	public abstract SSHConsoleConnection openConnection(
			IPDeviceAccessor accessor, SSHConsoleConnectionSettings settings)
			throws IOException;
	
}