package at.netcrawler.network.connection.telnet;

import at.netcrawler.network.connection.TCPIPConnectionSettings;


public class TelnetSettings extends TCPIPConnectionSettings {
	
	public static final int DEFAULT_PORT = 23;
	
	public TelnetSettings() {
		setPort(DEFAULT_PORT);
	}
	
	public TelnetSettings(TelnetSettings settings) {
		super(settings);
	}
	
	@Override
	public TelnetSettings clone() {
		return new TelnetSettings(this);
	}
	
}