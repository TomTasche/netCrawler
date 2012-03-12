package at.netcrawler.network.connection.ssh;

import at.netcrawler.network.connection.TCPIPConnectionSettings;


public class SSHSettings extends TCPIPConnectionSettings {
	
	public static final int DEFAULT_PORT = 22;
	
	private SSHVersion version;
	private String username;
	private String password;
	
	public SSHSettings() {
		setPort(DEFAULT_PORT);
	}
	
	public SSHSettings(SSHVersion version) {
		this();
		
		this.version = version;
	}
	
	public SSHSettings(SSHSettings settings) {
		super(settings);
		
		this.version = settings.version;
		this.username = settings.username;
		this.password = settings.password;
	}
	
	@Override
	public SSHSettings clone() {
		return new SSHSettings(this);
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