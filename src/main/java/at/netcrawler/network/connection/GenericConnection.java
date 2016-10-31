package at.netcrawler.network.connection;

import at.netcrawler.network.accessor.DeviceAccessor;


public abstract class GenericConnection<A extends DeviceAccessor, S extends ConnectionSettings> extends
		AbstractConnection {
	
	protected final A deviceAccessor;
	protected final S connectionSettings;
	
	@SuppressWarnings("unchecked")
	public GenericConnection(A accessor, S settings) {
		super(accessor, settings);
		
		this.deviceAccessor = accessor;
		this.connectionSettings = (S) settings.clone();
	}
	
	public final A getDeviceAccessor() {
		return deviceAccessor;
	}
	
	@SuppressWarnings("unchecked")
	public final S getConnectionSettings() {
		return (S) connectionSettings.clone();
	}
	
}