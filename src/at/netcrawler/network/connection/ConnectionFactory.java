package at.netcrawler.network.connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.accessor.DeviceAccessor;


public class ConnectionFactory {
	
	private Map<Class<? extends DeviceConnection>, ConnectionGateway> gatewayMap = new HashMap<Class<? extends DeviceConnection>, ConnectionGateway>();
	private Map<Class<? extends DeviceConnection>, ConnectionSettings> settingsMap = new HashMap<Class<? extends DeviceConnection>, ConnectionSettings>();
	
	public ConnectionFactory() {}
	
	public void addGateway(ConnectionGateway gateway) {
		gatewayMap.put(gateway.getConnectionClass(), gateway);
	}
	
	public void removeGateway(Class<? extends DeviceConnection> connectionClass) {
		gatewayMap.remove(connectionClass);
	}
	
	public void removeGateway(ConnectionGateway gateway) {
		removeGateway(gateway.getConnectionClass());
	}
	
	public void addConnectionSettings(ConnectionSettings settings) {
		settingsMap.put(settings.getConnectionClass(), settings.clone());
	}
	
	public void removeConnectionSettings(
			Class<? extends DeviceConnection> connectionClass) {
		settingsMap.remove(connectionClass);
	}
	
	public void removeConnectionSettings(ConnectionSettings settings) {
		removeConnectionSettings(settings.getConnectionClass());
	}
	
	@SuppressWarnings("unchecked")
	public <C extends DeviceConnection> C openConnection(
			DeviceAccessor accessor, ConnectionSettings settings,
			Class<C> connectionClass) throws IOException {
		ConnectionGateway gateway = gatewayMap.get(connectionClass);
		if (gateway == null) return null;
		
		return (C) gateway.openConnection(accessor, settings);
	}
	
	public <C extends DeviceConnection> C openConnection(
			DeviceAccessor accessor, Class<C> connectionClass)
			throws IOException {
		ConnectionSettings settings = settingsMap.get(connectionClass);
		if (settings == null) throw new IllegalArgumentException(
				"No default settings found!");
		
		return openConnection(accessor, settings, connectionClass);
	}
	
}