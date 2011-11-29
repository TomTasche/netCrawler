package at.netcrawler.network.connection.ssh;

import at.netcrawler.network.connection.TCPIPConnectionSettings;


public class SSHConnectionSettings extends TCPIPConnectionSettings {
	
	public static final int DEFAULT_PORT = 22;
	
	private SSHVersion version;
	private String username;
	private String password;
	
	public SSHConnectionSettings() {
		setPort(DEFAULT_PORT);
	}
	
	public SSHConnectionSettings(SSHConnectionSettings settings) {
		super(settings);
	}
	
	public SSHVersion getVersion() {
		return version;
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
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}