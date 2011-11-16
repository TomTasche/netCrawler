package at.netcrawler.network.connection.snmp;

import at.netcrawler.network.connection.ConnectionSettings;


public class SNMPConnectionSettings extends ConnectionSettings {
	
	public static final int DEFAULT_PORT = 161;
	public static final int DEFAULT_RETRIES = 1;
	public static final int DEFAULT_TIMEOUT = 1500;
	
	
	private SNMPVersion version;
	private int port;
	private String community;
	private SNMPSecurityLevel securityLevel;
	private String username;
	private String password;
	private String cryptoKey;
	private int retries;
	
	
	public SNMPConnectionSettings() {
		port = DEFAULT_PORT;
		retries = DEFAULT_RETRIES;
		timeout = DEFAULT_TIMEOUT;
	}
	public SNMPConnectionSettings(SNMPConnectionSettings settings) {
		super(settings);
		
		version = settings.version;
		port = settings.port;
		community = settings.community;
		securityLevel = settings.securityLevel;
		username = settings.username;
		password = settings.password;
		cryptoKey = settings.cryptoKey;
	}
	
	public SNMPVersion getVersion() {
		return version;
	}
	public int getPort() {
		return port;
	}
	public String getCommunity() {
		return community;
	}
	public SNMPSecurityLevel getSecurityLevel() {
		return securityLevel;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getCryptoKey() {
		return cryptoKey;
	}
	public int getRetries() {
		return retries;
	}
	
	public void setVersion(SNMPVersion version) {
		this.version = version;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void setCommunity(String community) {
		this.community = community;
	}
	public void setSecurityLevel(SNMPSecurityLevel securityLevel) {
		this.securityLevel = securityLevel;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setCryptoKey(String cryptoKey) {
		this.cryptoKey = cryptoKey;
	}
	public void setRetries(int retries) {
		this.retries = retries;
	}
	
}