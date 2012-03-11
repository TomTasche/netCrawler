package at.netcrawler.network.connection;

import java.util.HashMap;
import java.util.Map;

import at.netcrawler.network.accessor.DeviceAccessor;


public class ConnectionSettingsManager {
	
	private final Map<ConnectionType, ConnectionSettings> defaultSettingsMap = new HashMap<ConnectionType, ConnectionSettings>();
	private final Map<DeviceAccessor, Map<ConnectionType, ConnectionSettings>> deviceSettingsMap = new HashMap<DeviceAccessor, Map<ConnectionType, ConnectionSettings>>();
	
	public ConnectionSettings getDefaultSettings(ConnectionType connectionType) {
		return defaultSettingsMap.get(connectionType);
	}
	
	public ConnectionSettings getDeviceSettings(DeviceAccessor deviceAccessor,
			ConnectionType connectionType) {
		Map<ConnectionType, ConnectionSettings> connectionSettingsMap = deviceSettingsMap
				.get(deviceAccessor);
		if (connectionSettingsMap == null) return null;
		
		return connectionSettingsMap.get(connectionType);
	}
	
	public ConnectionSettings getSettings(DeviceAccessor deviceAccessor,
			ConnectionType connectionType) {
		ConnectionSettings result = getDeviceSettings(deviceAccessor,
				connectionType);
		if (result == null) result = getDefaultSettings(connectionType);
		return result;
	}
	
	public void setDefaultSettings(ConnectionType connectionType,
			ConnectionSettings connectionSettings) {
		defaultSettingsMap.put(connectionType, connectionSettings);
	}
	
	public void setDeviceSettings(DeviceAccessor deviceAccessor,
			ConnectionType connectionType, ConnectionSettings connectionSettings) {
		Map<ConnectionType, ConnectionSettings> connectionSettingsMap = deviceSettingsMap
				.get(deviceAccessor);
		if (connectionSettingsMap == null) {
			connectionSettingsMap = new HashMap<ConnectionType, ConnectionSettings>();
			deviceSettingsMap.put(deviceAccessor, connectionSettingsMap);
		}
		
		connectionSettingsMap.put(connectionType, connectionSettings);
	}
	
	public void removeDefaultSettings(ConnectionType connectionType) {
		defaultSettingsMap.remove(connectionType);
	}
	
	public void removeDeviceSettings(DeviceAccessor deviceAccessor,
			ConnectionType connectionType) {
		Map<ConnectionType, ConnectionSettings> connectionSettingsMap = deviceSettingsMap
				.get(deviceAccessor);
		if (connectionSettingsMap == null) return;
		
		connectionSettingsMap.remove(connectionType);
	}
	
}