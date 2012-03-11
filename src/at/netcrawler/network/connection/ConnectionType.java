package at.netcrawler.network.connection;

import at.netcrawler.network.connection.snmp.SNMPConnection;
import at.netcrawler.network.connection.ssh.SSHConnection;
import at.netcrawler.network.connection.telnet.TelnetConnection;


public enum ConnectionType {
	
	TELNET(TelnetConnection.class),
	SSH(SSHConnection.class),
	SNMP(SNMPConnection.class);
	
	private final Class<? extends Connection> connectionClass;
	
	private ConnectionType(Class<? extends Connection> connectionClass) {
		this.connectionClass = connectionClass;
	}
	
	public Class<? extends Connection> getConnectionClass() {
		return connectionClass;
	}
	
}