package at.netcrawler.network.connection;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public abstract class TCPIPDeviceConnection extends IPDeviceConnection {
	
	public TCPIPDeviceConnection(IPDeviceAccessor accessor,
			TCPIPConnectionSettings settings) {
		super(accessor, settings);
	}
	
}