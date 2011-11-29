package at.netcrawler.network.connection.ssh.console;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.SSHConnection;
import at.netcrawler.network.connection.ssh.SSHConnectionSettings;


public abstract class SSHConsoleConnection extends SSHConnection implements
		SSHConsole {
	
	public SSHConsoleConnection(IPDeviceAccessor accessor,
			SSHConnectionSettings settings) {
		super(accessor, settings);
	}
	
}