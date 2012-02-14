package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;


public abstract class IPConnectionGateway<S extends ConnectionSettings> extends
		GenericConnectionGateway<IPDeviceAccessor, S> {
	
	@Override
	public abstract Class<? extends IPDeviceConnection> getConnectionClass();
	
	@Override
	public Class<IPDeviceAccessor> getAccessorClass() {
		return IPDeviceAccessor.class;
	}
	
	@Override
	protected abstract IPDeviceConnection openConnectionGenericImpl(
			IPDeviceAccessor accessor, S settings) throws IOException;
	
}