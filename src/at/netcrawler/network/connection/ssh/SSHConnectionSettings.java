package at.netcrawler.network.connection.ssh;

import at.netcrawler.network.connection.ConnectionSettings;


public class SSHConnectionSettings extends ConnectionSettings {
	
	public static final int DEFAULT_PORT = 22;
	
	private SSHVersion version;
	private int port;
	private String username;
	private String password;
	
	public SSHConnectionSettings() {
		port = DEFAULT_PORT;
	}
	public SSHConnectionSettings(SSHConnectionSettings settings) {
		super(settings);
		
		port = settings.port;
	}
	
	public SSHVersion getVersion() {
		return version;
	}
	public int getPort() {
		return port;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	
	public void setVersion(SSHVersion version) {
		this.version = version;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}