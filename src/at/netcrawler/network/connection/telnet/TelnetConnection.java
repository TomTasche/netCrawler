package at.netcrawler.network.connection.telnet;

import at.netcrawler.network.connection.IPDeviceConnection;


public abstract class TelnetConnection extends
		IPDeviceConnection<TelnetSettings> implements TelnetClient {
	
	@Override
	public Class<TelnetSettings> getSettingsClass() {
		return TelnetSettings.class;
	}
	
}