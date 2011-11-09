package at.netcrawler.network.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public enum NetworkDeviceExtensionSet implements NetworkModelExtensionSet {
	
	ROUTER {
		{
			addExtension(NetworkDeviceExtension.ROUTER_ROUTING_TABLE);
		}
	};
	
	
	
	
	private Set<NetworkDeviceExtensionSet> superExtensionSets = new HashSet<NetworkDeviceExtensionSet>();
	private Map<String, Class<?>> typeMap = new HashMap<String, Class<?>>();
	
	
	
	@Override
	public Class<NetworkDevice> getExtendedClass() {
		return NetworkDevice.class;
	}
	
	@Override
	public Set<NetworkDeviceExtensionSet> getSuperExtensionSets() {
		return superExtensionSets;
	}
	
	@Override
	public Map<String, Class<?>> getTypeMap() {
		return typeMap;
	}
	
	
	void addSuperExtensionSet(NetworkDeviceExtensionSet extensionSet) {
		superExtensionSets.add(extensionSet);
	}
	
	void addExtension(NetworkDeviceExtension extension) {
		typeMap.put(extension.getKey(), extension.getType());
	}
	
}