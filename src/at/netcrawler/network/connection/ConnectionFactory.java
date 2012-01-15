package at.netcrawler.network.connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.accessor.DeviceAccessor;


public class ConnectionFactory {
	
	private Map<Class<? extends Connection>, ConnectionGateway> gatewayMap = new HashMap<Class<? extends Connection>, ConnectionGateway>();
	private Map<Class<? extends Connection>, ConnectionSettings> settingsMap = new HashMap<Class<? extends Connection>, ConnectionSettings>();
	
	public ConnectionFactory() {}
	
	public ConnectionFactory(ConnectionGateway... gateways) {
		for (ConnectionGateway gateway : gateways) {
			addGateway(gateway);
		}
	}
	
	public void addGateway(ConnectionGateway gateway) {
		gatewayMap.put(gateway.getConnectionClass(), gateway);
	}
	
	public void removeGateway(Class<? extends Connection> connectionClass) {
		gatewayMap.remove(connectionClass);
	}
	
	public void removeGateway(ConnectionGateway gateway) {
		removeGateway(gateway.getConnectionClass());
	}
	
	public void addConnectionSettings(ConnectionSettings settings) {
		settingsMap.put(settings.getConnectionClass(), settings.clone());
	}
	
	public void removeConnectionSettings(
			Class<? extends Connection> connectionClass) {
		settingsMap.remove(connectionClass);
	}
	
	public void removeConnectionSettings(ConnectionSettings settings) {
		removeConnectionSettings(settings.getConnectionClass());
	}
	
	public Connection openConnection(DeviceAccessor accessor,
			ConnectionSettings settings) throws IOException {
		return openConnection(accessor, settings, settings.getConnectionClass());
	}
	
	@SuppressWarnings("unchecked")
	public <C extends Connection> C openConnection(DeviceAccessor accessor,
			ConnectionSettings settings, Class<C> connectionClass)
			throws IOException {
		ConnectionGateway gateway = gatewayMap.get(connectionClass);
		if (gateway == null) return null;
		
		return (C) gateway.openConnection(accessor, settings);
	}
	
	public <C extends Connection> C openConnection(DeviceAccessor accessor,
			Class<C> connectionClass) throws IOException {
		ConnectionSettings settings = settingsMap.get(connectionClass);
		if (settings == null) throw new IllegalArgumentException(
				"No default settings found!");
		
		return openConnection(accessor, settings, connectionClass);
	}
	
}