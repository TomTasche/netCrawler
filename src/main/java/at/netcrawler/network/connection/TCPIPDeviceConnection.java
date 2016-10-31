package at.netcrawler.network.connection;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public abstract class TCPIPDeviceConnection<S extends TCPIPConnectionSettings> extends
		IPDeviceConnection<S> {
	
	public TCPIPDeviceConnection(IPDeviceAccessor accessor, S settings) {
		super(accessor, settings);
	}
	
}