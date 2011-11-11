package at.netcrawler.network.connection.snmp;

import java.io.IOException;

import at.netcrawler.network.IPDeviceAccessor;
import at.netcrawler.network.connection.IPConnectionGateway;


public abstract class SNMPConnectionGateway extends
		IPConnectionGateway<SNMPConnectionSettings> {
	
	@Override
	public Class<SNMPConnectionSettings> getConnectionSettingsClass() {
		return SNMPConnectionSettings.class;
	}
	
	@Override
	public abstract SNMPConnection openConnection(IPDeviceAccessor accessor,
			SNMPConnectionSettings settings) throws IOException;
	
}