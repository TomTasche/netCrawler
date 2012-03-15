package at.netcrawler.ui.assistant;

import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.connection.ConnectionSettings;
import at.netcrawler.network.connection.ConnectionType;
import at.netcrawler.network.connection.TCPIPConnectionSettings;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;
import at.netcrawler.network.connection.telnet.TelnetSettings;


// TODO: container for connection class
public enum ConnectionContainer {
	
	NOTHING("- Choose connection -", null, null),
	TELNET("Telnet", ConnectionType.TELNET, new TelnetSettings()),
	SSH1("SSH v1", ConnectionType.SSH, new SSHSettings(SSHVersion.VERSION1)),
	SSH2("SSH v2", ConnectionType.SSH, new SSHSettings(SSHVersion.VERSION2));
	
	private static final Map<String, ConnectionContainer> NAME_MAP = new HashMap<String, ConnectionContainer>();
	
	static {
		for (ConnectionContainer container : values()) {
			NAME_MAP.put(container.name, container);
		}
	}
	
	public static ConnectionContainer getContainerByName(String name) {
		return NAME_MAP.get(name);
	}
	
	public static ConnectionSettings getSettings(Configuration configuration) {
		ConnectionContainer connectionType = configuration
				.getConnectionContainer();
		
		switch (connectionType) {
		case TELNET:
			TelnetSettings telnetSettings = new TelnetSettings();
			telnetSettings.setPort(configuration.getPort());
			return telnetSettings;
		case SSH1:
		case SSH2:
			SSHSettings sshSettings = new SSHSettings();
			SSHVersion version = (connectionType == SSH1) ? SSHVersion.VERSION1
					: SSHVersion.VERSION2;
			sshSettings.setVersion(version);
			sshSettings.setPort(configuration.getPort());
			sshSettings.setUsername(configuration.getUsername());
			sshSettings.setPassword(configuration.getPassword());
			return sshSettings;
		default:
			throw new IllegalStateException("Unsupported connection type");
		}
	}
	
	private final String name;
	private final ConnectionType connectionType;
	private final ConnectionSettings defaultSettings;
	
	private ConnectionContainer(String name, ConnectionType connectionType,
			ConnectionSettings defaultSettings) {
		this.name = name;
		this.connectionType = connectionType;
		this.defaultSettings = defaultSettings;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public ConnectionType getConnectionType() {
		return connectionType;
	}
	
	public ConnectionSettings getDefaultSettings() {
		return defaultSettings;
	}
	
	public int getDefaultPort() {
		return ((TCPIPConnectionSettings) defaultSettings).getPort();
	}
	
	public boolean legalConnection() {
		return ordinal() > 0;
	}
	
}