package at.netcrawler.network.connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.accessor.DeviceAccessor;


public class ConnectionBuilder {
	
	private Map<ConnectionType, ConnectionGateway> gatewayMap = new HashMap<ConnectionType, ConnectionGateway>();
	
	private ConnectionSettingsManager settingsManager;
	
	public ConnectionBuilder() {}
	
	public ConnectionBuilder(ConnectionSettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}
	
	public ConnectionBuilder(ConnectionGateway... connectionGateways) {
		for (ConnectionGateway connectionGateway : connectionGateways) {
			addConnectionGateway(connectionGateway.getConnectionType(),
					connectionGateway);
		}
	}
	
	public ConnectionBuilder(ConnectionSettingsManager settingsManager,
			ConnectionGateway... connectionGateways) {
		this(connectionGateways);
		
		this.settingsManager = settingsManager;
	}
	
	public ConnectionSettingsManager getSettingsManager() {
		return settingsManager;
	}
	
	public void setSettingsManager(ConnectionSettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}
	
	public void addConnectionGateway(ConnectionType connectionType,
			ConnectionGateway connectionGateway) {
		gatewayMap.put(connectionType, connectionGateway);
	}
	
	public void removeConnectionGateway(ConnectionType connectionType) {
		gatewayMap.remove(connectionType);
	}
	
	public void removeConnectionGateway(ConnectionGateway connectionGateway) {
		removeConnectionGateway(connectionGateway.getConnectionType());
	}
	
	@SuppressWarnings("unchecked")
	public <C extends Connection> C openConnection(
			ConnectionType connectionType, DeviceAccessor deviceAccessor,
			ConnectionSettings connectionSettings) throws IOException {
		ConnectionGateway gateway = gatewayMap.get(connectionType);
		if (gateway == null) return null;
		
		return (C) gateway.openConnection(deviceAccessor, connectionSettings);
	}
	
	public <C extends Connection> C openConnection(
			ConnectionType connectionType, DeviceAccessor deviceAccessor)
			throws IOException {
		if (settingsManager == null)
			throw new IllegalStateException("No settings manager given");
		
		ConnectionSettings connectionSettings = settingsManager.getSettings(
				deviceAccessor, connectionType);
		if (connectionSettings == null)
			throw new IllegalStateException("No matching settings found");
		
		return openConnection(connectionType, deviceAccessor,
				connectionSettings);
	}
	
}