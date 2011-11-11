package at.netcrawler.network.connection.telnet;

import java.io.IOException;

import at.netcrawler.network.IPDeviceAccessor;
import at.netcrawler.network.connection.IPConnectionGateway;


public abstract class TelnetConnectionGateway extends
		IPConnectionGateway<TelnetConnectionSettings> {
	
	@Override
	public Class<TelnetConnectionSettings> getConnectionSettingsClass() {
		return TelnetConnectionSettings.class;
	}
	
	@Override
	public abstract TelnetConnection openConnection(IPDeviceAccessor accessor,
			TelnetConnectionSettings settings) throws IOException;
	
}