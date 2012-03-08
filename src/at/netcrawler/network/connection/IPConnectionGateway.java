package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public abstract class IPConnectionGateway<C extends IPDeviceConnection, S extends ConnectionSettings> extends
		GenericConnectionGateway<C, IPDeviceAccessor, S> {
	
	@Override
	public Class<IPDeviceAccessor> getAccessorClass() {
		return IPDeviceAccessor.class;
	}
	
	@Override
	protected abstract C openConnectionGenericImpl(IPDeviceAccessor accessor,
			S settings) throws IOException;
	
}