package at.netcrawler.network.connection.telnet;

import at.netcrawler.network.connection.ConnectionSettings;


public class TelnetConnectionSettings extends ConnectionSettings {
	
	public static final int DEFAULT_PORT = 23;
	
	private int port;
	
	public TelnetConnectionSettings() {}
	
	public TelnetConnectionSettings(TelnetConnectionSettings settings) {
		super(settings);
		
		port = settings.port;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
}