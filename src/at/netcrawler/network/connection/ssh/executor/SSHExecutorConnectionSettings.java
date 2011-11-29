package at.netcrawler.network.connection.ssh.executor;

import at.netcrawler.network.connection.ssh.SSHConnectionSettings;


public class SSHExecutorConnectionSettings extends SSHConnectionSettings {
	
	public static final int DEFAULT_TIMEOUT = 5000;
	
	public SSHExecutorConnectionSettings() {
		setTimeout(DEFAULT_TIMEOUT);
	}
	
	public SSHExecutorConnectionSettings(SSHExecutorConnectionSettings settings) {
		super(settings);
	}
	
}