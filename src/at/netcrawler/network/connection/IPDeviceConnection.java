package at.netcrawler.network.connection;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public abstract class IPDeviceConnection<S extends ConnectionSettings> extends
		GenericDeviceConnection<IPDeviceAccessor, S> {
	
	@Override
	public Class<IPDeviceAccessor> getAccessorClass() {
		return IPDeviceAccessor.class;
	}
	
}