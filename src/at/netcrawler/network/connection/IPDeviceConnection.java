package at.netcrawler.network.connection;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public abstract class IPDeviceConnection<S extends ConnectionSettings> extends
		GenericConnection<IPDeviceAccessor, S> {
	
	public IPDeviceConnection(IPDeviceAccessor accessor, S settings) {
		super(accessor, settings);
	}
	
}