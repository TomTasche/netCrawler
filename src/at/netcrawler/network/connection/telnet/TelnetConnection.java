package at.netcrawler.network.connection.telnet;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.TCPIPDeviceConnection;


public abstract class TelnetConnection extends TCPIPDeviceConnection implements
		TelnetClient {
	
	public TelnetConnection(IPDeviceAccessor accessor, TelnetSettings settings) {
		super(accessor, settings);
	}
	
}