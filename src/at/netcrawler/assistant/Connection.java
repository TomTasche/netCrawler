package at.netcrawler.assistant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public enum Connection {
	
	NOTHING("- Choose connection -", -1), TELNET("Telnet", 23), SSH1("SSH v1",
			22), SSH2("SSH v2", 22);
	
	private static final Map<String, Connection> NAME_MAP;
	
	static {
		Map<String, Connection> nameMap = new HashMap<String, Connection>();
		
		for (Connection connection : values()) {
			nameMap.put(
					connection.name, connection);
		}
		
		NAME_MAP = Collections.unmodifiableMap(nameMap);
	}
	
	public static Connection getConnectionByName(String name) {
		return NAME_MAP.get(name);
	}
	
	private final String name;
	private final int defaultPort;
	
	private Connection(String name, int defaultPort) {
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