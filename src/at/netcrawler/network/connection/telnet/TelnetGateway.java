package at.netcrawler.network.connection.telnet;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.TCPIPConnectionGateway;


public abstract class TelnetGateway extends
		TCPIPConnectionGateway<TelnetSettings> {
	
	@Override
	public final Class<TelnetConnection> getConnectionClass() {
		return TelnetConnection.class;
	}
	
	@Override
	public final Class<TelnetSettings> getSettingsClass() {
		return TelnetSettings.class;
	}
	
	@Override
	protected abstract TelnetConnection openConnectionGenericImpl(
			IPDeviceAccessor accessor, TelnetSettings settings)
			throws IOException;
	
}