package at.netcrawler.ui.crawler;

import at.andiwand.library.network.ip.IPv4Address;


public class CrawlSettings {
	
	private IPv4Address address;
	private String defaultUsername;
	private String defaultPassword;
	
	public IPv4Address getAddress() {
		return address;
	}
	
	public void setAddress(IPv4Address address) {
		this.address = address;
	}
	
	public String getDefaultUsername() {
		return defaultUsername;
	}
	
	public void setDefaultUsername(String defaultUsername) {
		this.defaultUsername = defaultUsername;
	}
	
	public String getDefaultPassword() {
		return defaultPassword;
	}
	
	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}
	
}