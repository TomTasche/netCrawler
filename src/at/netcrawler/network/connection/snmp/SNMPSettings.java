package at.netcrawler.network.connection.snmp;

import at.netcrawler.network.connection.TCPIPConnectionSettings;


public class SNMPSettings extends TCPIPConnectionSettings {
	
	public static final int DEFAULT_PORT = 161;
	public static final int DEFAULT_RETRIES = 1;
	public static final int DEFAULT_TIMEOUT = 1500;
	
	private SNMPVersion version;
	private String community;
	private SNMPSecurityLevel securityLevel;
	private String username;
	private String password;
	private String cryptoKey;
	private int retries;
	
	public SNMPSettings() {
		setPort(DEFAULT_PORT);
		setRetries(DEFAULT_RETRIES);
		setTimeout(DEFAULT_TIMEOUT);
	}
	
	public SNMPSettings(SNMPSettings settings) {
		super(settings);
		
		setVersion(settings.version);
		setCommunity(settings.community);
		setSecurityLevel(settings.securityLevel);
		setUsername(settings.username);
		setPassword(settings.password);
		setCryptoKey(settings.cryptoKey);
	}
	
	@Override
	public SNMPSettings clone() {
		return new SNMPSettings(this);
	}
	
	public SNMPVersion getVersion() {
		return version;
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