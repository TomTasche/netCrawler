package at.netcrawler.network.connection.telnet;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.CommandLineConnection;
import at.netcrawler.network.connection.ConnectionType;
import at.netcrawler.network.connection.TCPIPDeviceConnection;


public abstract class TelnetConnection extends
		TCPIPDeviceConnection<TelnetSettings> implements TelnetClient,
		CommandLineConnection {
	
	public TelnetConnection(IPDeviceAccessor accessor, TelnetSettings settings) {
		super(accessor, settings);
	}
	
	@Override
	public final ConnectionType getConnectionType() {
		return ConnectionType.TELNET;
	}
	
}