package at.netcrawler.network.connection.snmp;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.TCPIPConnectionGateway;


public abstract class SNMPGateway extends TCPIPConnectionGateway<SNMPSettings> {
	
	@Override
	public final Class<SNMPConnection> getConnectionClass() {
		return SNMPConnection.class;
	}
	
	@Override
	public final Class<SNMPSettings> getSettingsClass() {
		return SNMPSettings.class;
	}
	
	@Override
	protected abstract SNMPConnection openConnectionGenericImpl(
			IPDeviceAccessor accessor, SNMPSettings settings)
			throws IOException;
	
}