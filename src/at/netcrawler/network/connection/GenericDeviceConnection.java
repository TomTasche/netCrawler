package at.netcrawler.network.connection;

import java.io.IOException;

import at.netcrawler.network.accessor.DeviceAccessor;


public abstract class GenericDeviceConnection<A extends DeviceAccessor, S extends ConnectionSettings>
		extends DeviceConnection {
	
	private A accessor;
	private S settings;
	
	@Override
	public abstract Class<A> getAccessorClass();
	
	@Override
	public abstract Class<S> getSettingsClass();
	
	@Override
	public final A getAccessor() {
		return accessor;
	}
	
	@Override
	public final S getSettings() {
		return settings;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected final void connectImpl(DeviceAccessor accessor,
			ConnectionSettings settings) throws IOException {
		if (!getAccessorClass().equals(accessor.getClass())) throw new IllegalArgumentException(
				"Illegal accessor class!");
		if (!getSettingsClass().equals(settings.getClass())) throw new IllegalArgumentException(
				"Illegal settings class!");
		
		connectGenericImpl((A) accessor, (S) settings);
		
		this.accessor = (A) accessor;
		this.settings = (S) settings;
	}
	
	protected abstract void connectGenericImpl(A accessor, S settings)
			throws IOException;
	
}