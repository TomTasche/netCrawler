package at.netcrawler.network.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public enum NetworkCableExtensionSet implements NetworkModelExtensionSet {
	
	ETHERNET {
		{
			addExtension(NetworkCableExtension.ETHERNET_CROSSED);
		}
	};
	
	
	
	
	private Set<NetworkCableExtensionSet> superExtensionSets = new HashSet<NetworkCableExtensionSet>();
	private Map<String, Class<?>> typeMap = new HashMap<String, Class<?>>();
	
	
	
	@Override
	public Class<NetworkCable> getExtendedClass() {
		return NetworkCable.class;
	}
	
	@Override
	public Set<NetworkCableExtensionSet> getSuperExtensionSets() {
		return superExtensionSets;
	}
	
	@Override
	public Map<String, Class<?>> getTypeMap() {
		return typeMap;
	}
	
	
	void addSuperExtensionSet(NetworkCableExtensionSet extensionSet) {
		superExtensionSets.add(extensionSet);
	}
	
	void addExtension(NetworkCableExtension extension) {
		typeMap.put(extension.getKey(), extension.getType());
	}
	
}