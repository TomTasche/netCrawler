package at.netcrawler.assistant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public enum ConnectionType {
	
	NOTHING("- Choose connection -", -1), TELNET("Telnet", 23), SSH1("SSH v1",
			22), SSH2("SSH v2", 22);
	
	private static final Map<String, ConnectionType> NAME_MAP;
	
	static {
		Map<String, ConnectionType> nameMap = new HashMap<String, ConnectionType>();
		
		for (ConnectionType connection : values()) {
			nameMap.put(connection.name, connection);
		}
		
		NAME_MAP = Collections.unmodifiableMap(nameMap);
	}
	
	public static ConnectionType getConnectionByName(String name) {
		return NAME_MAP.get(name);
	}
	
	private final String name;
	private final int defaultPort;
	
	private ConnectionType(String name, int defaultPort) {
		this.name = name;
		this.defaultPort = defaultPort;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getDefaultPort() {
		return defaultPort;
	}
	
	public boolean legalConnection() {
		return ordinal() > 0;
	}
	
}