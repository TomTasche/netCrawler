package at.netcrawler.network.connection.telnet;

import at.netcrawler.network.connection.ConnectionType;
import at.netcrawler.network.connection.TCPIPConnectionGateway;


public abstract class TelnetGateway<C extends TelnetConnection> extends
		TCPIPConnectionGateway<C, TelnetSettings> {
	
	@Override
	public final ConnectionType getConnectionType() {
		return ConnectionType.TELNET;
	}
	
	@Override
	public final Class<TelnetSettings> getSettingsClass() {
		return TelnetSettings.class;
	}
	
}