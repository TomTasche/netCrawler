package at.netcrawler.network.connection.telnet;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.IPDeviceConnection;


public abstract class TelnetConnection extends IPDeviceConnection implements
		TelnetClient {
	
	public TelnetConnection(IPDeviceAccessor accessor,
			TelnetConnectionSettings settings) {
		super(accessor, settings);
	}
	
}