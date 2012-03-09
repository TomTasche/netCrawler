package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.DeviceAccessor;


public abstract class GenericConnectionGateway<C extends Connection, A extends DeviceAccessor, S extends ConnectionSettings>
		extends ConnectionGateway {
	
	@Override
	public abstract Class<C> getConnectionClass();
	
	@Override
	public abstract Class<A> getAccessorClass();
	
	@Override
	public abstract Class<S> getSettingsClass();
	
	@Override
	@SuppressWarnings("unchecked")
	protected final C openConnectionImpl(DeviceAccessor accessor,
			ConnectionSettings settings) throws IOException {
		return openConnectionGenericImpl(
				(A) accessor, (S) settings);
	}
	
	protected abstract C openConnectionGenericImpl(A accessor, S settings)
			throws IOException;
	
}