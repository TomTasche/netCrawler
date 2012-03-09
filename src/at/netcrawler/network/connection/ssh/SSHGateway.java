package at.netcrawler.network.connection.ssh;

import at.netcrawler.network.connection.ConnectionType;
import at.netcrawler.network.connection.TCPIPConnectionGateway;


public abstract class SSHGateway<C extends SSHConnection> extends
		TCPIPConnectionGateway<C, SSHSettings> {
	
	@Override
	public ConnectionType getConnectionType() {
		return ConnectionType.SSH;
	}
	
	@Override
	public final Class<SSHSettings> getSettingsClass() {
		return SSHSettings.class;
	}
	
}