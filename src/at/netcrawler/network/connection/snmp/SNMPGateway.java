package at.netcrawler.network.connection.snmp;

import at.netcrawler.network.connection.ConnectionType;
import at.netcrawler.network.connection.TCPIPConnectionGateway;


public abstract class SNMPGateway<C extends SNMPConnection> extends
		TCPIPConnectionGateway<C, SNMPSettings> {
	
	@Override
	public ConnectionType getConnectionType() {
		return ConnectionType.SNMP;
	}
	
	@Override
	public final Class<SNMPSettings> getSettingsClass() {
		return SNMPSettings.class;
	}
	
}