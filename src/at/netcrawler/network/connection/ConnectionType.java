package at.netcrawler.network.connection;

import at.netcrawler.network.connection.snmp.SNMPConnection;
import at.netcrawler.network.connection.snmp.SNMPSettings;
import at.netcrawler.network.connection.ssh.SSHConnection;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.connection.telnet.TelnetConnection;
import at.netcrawler.network.connection.telnet.TelnetSettings;


public enum ConnectionType {
	
	TELNET(TelnetConnection.class, new TelnetSettings()),
	SSH(SSHConnection.class, new SSHSettings()),
	SNMP(SNMPConnection.class, new SNMPSettings());
	
	private final Class<? extends Connection> connectionClass;
	private final ConnectionSettings settings;
	
	private ConnectionType(Class<? extends Connection> connectionClass,
			ConnectionSettings settings) {
		this.connectionClass = connectionClass;
		this.settings = settings;
	}
	
	public Class<? extends Connection> getConnectionClass() {
		return connectionClass;
	}
	
	public ConnectionSettings getSettings() {
		return settings.clone();
	}
	
}