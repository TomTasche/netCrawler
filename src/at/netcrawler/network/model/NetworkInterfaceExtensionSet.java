package at.netcrawler.network.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public enum NetworkInterfaceExtensionSet implements NetworkModelExtensionSet {
	
	ETHERNET {
		{
			addExtension(NetworkInterfaceExtension.ETHERNET_ADDRESS);
		}
	},
	IPV4 {
		{
			addExtension(NetworkInterfaceExtension.IPV4_ADDRESS);
			addExtension(NetworkInterfaceExtension.IPV4_SUBNET_MASK);
		}
	};
	
	
	
	
	private Set<NetworkInterfaceExtensionSet> superExtensionSets = new HashSet<NetworkInterfaceExtensionSet>();
	private Map<String, Class<?>> typeMap = new HashMap<String, Class<?>>();
	
	
	
	@Override
	public Class<NetworkInterface> getExtendedClass() {
		return NetworkInterface.class;
	}
	
	@Override
	public Set<NetworkInterfaceExtensionSet> getSuperExtensionSets() {
		return superExtensionSets;
	}
	
	@Override
	public Map<String, Class<?>> getTypeMap() {
		return typeMap;
	}
	
	
	void addSuperExtensionSet(NetworkInterfaceExtensionSet extensionSet) {
		superExtensionSets.add(extensionSet);
	}
	
	void addExtension(NetworkInterfaceExtension extension) {
		typeMap.put(extension.getKey(), extension.getType());
	}
	
}