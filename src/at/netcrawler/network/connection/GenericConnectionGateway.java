package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.DeviceAccessor;


public abstract class GenericConnectionGateway<A extends DeviceAccessor, S extends ConnectionSettings> extends
		ConnectionGateway {
	
	@Override
	public abstract Class<? extends DeviceConnection> getConnectionClass();
	
	@Override
	public abstract Class<A> getAccessorClass();
	
	@Override
	public abstract Class<S> getSettingsClass();
	
	@Override
	@SuppressWarnings("unchecked")
	protected final DeviceConnection openConnectionImpl(DeviceAccessor accessor,
			ConnectionSettings settings) throws IOException {
		return openConnectionGenericImpl((A) accessor, (S) settings);
	}
	
	protected abstract DeviceConnection openConnectionGenericImpl(A accessor,
			S settings) throws IOException;
	
}