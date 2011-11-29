package at.netcrawler.network.connection.telnet;

import at.netcrawler.network.connection.TCPIPConnectionSettings;


public class TelnetConnectionSettings extends TCPIPConnectionSettings {
	
	public static final int DEFAULT_PORT = 23;
	
	public TelnetConnectionSettings() {
		setPort(DEFAULT_PORT);
	}
	
	public TelnetConnectionSettings(TelnetConnectionSettings settings) {
		super(settings);
	}
	
}