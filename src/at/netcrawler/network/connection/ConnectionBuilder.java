package at.netcrawler.network.connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.accessor.DeviceAccessor;


public class ConnectionBuilder {
	
	private Map<ConnectionType, ConnectionGateway> gatewayMap = new HashMap<ConnectionType, ConnectionGateway>();
	private Map<ConnectionType, ConnectionSettings> settingsMap = new HashMap<ConnectionType, ConnectionSettings>();
	
	public ConnectionBuilder() {}
	
	public ConnectionBuilder(ConnectionGateway... gateways) {
		for (ConnectionGateway gateway : gateways) {
			addGateway(gateway);
		}
	}
	
	public void addGateway(ConnectionGateway gateway) {
		gatewayMap.put(gateway.getConnectionType(), gateway);
	}
	
	public void removeGateway(ConnectionType connectionType) {
		gatewayMap.remove(connectionType);
	}
	
	public void removeGateway(ConnectionGateway gateway) {
		removeGateway(gateway.getConnectionType());
	}
	
	public void addConnectionSettings(ConnectionType connectionType,
			ConnectionSettings settings) {
		settingsMap.put(connectionType, settings.clone());
	}
	
	public void removeConnectionSettings(ConnectionType connectionType) {
		settingsMap.remove(connectionType);
	}
	
	@SuppressWarnings("unchecked")
	public <C extends Connection> C openConnection(
			ConnectionType connectionType, DeviceAccessor accessor,
			ConnectionSettings settings) throws IOException {
		ConnectionGateway gateway = gatewayMap.get(connectionType);
		if (gateway == null) return null;
		
		return (C) gateway.openConnection(accessor, settings);
	}
	
	public <C extends Connection> C openConnection(
			ConnectionType connectionType, DeviceAccessor accessor)
			throws IOException {
		ConnectionSettings settings = settingsMap.get(connectionType);
		if (settings == null)
			throw new IllegalArgumentException("No default settings found");
		
		return openConnection(connectionType, accessor, settings);
	}
	
}