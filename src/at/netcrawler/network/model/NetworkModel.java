package at.netcrawler.network.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class NetworkModel {
	
	private final List<NetworkModelListener> listeners = new ArrayList<NetworkModelListener>();
	
	private final Map<String, Class<?>> initialTypeMap;
	private Map<String, Class<?>> typeMap;
	private final Map<String, Object> valueMap = new HashMap<String, Object>();
	private final Set<NetworkModelExtensionSet> extensionSets = new HashSet<NetworkModelExtensionSet>();
	
	
	
	public NetworkModel(Map<String, Class<?>> initialTypeMap) {
		this.initialTypeMap = initialTypeMap;
		typeMap = new HashMap<String, Class<?>>(initialTypeMap);
	}
	
	
	
	@Override
	public String toString() {
		return valueMap.toString();
	}
	
	
	public Object getValue(String key) {
		return valueMap.get(key);
	}
	public Object getValue(NetworkModelExtension extension) {
		return getValue(extension.getKey());
	}
	public Map<String, Class<?>> getTypeMap() {
		return new HashMap<String, Class<?>>(typeMap);
	}
	public Map<String, Object> getValueMap() {
		return new HashMap<String, Object>(valueMap);
	}
	public Set<NetworkModelExtensionSet> getExtensionSets() {
		return new HashSet<NetworkModelExtensionSet>(extensionSets);
	}
	public boolean hasExtensionSet(NetworkModelExtensionSet extensionSet) {
		return extensionSets.contains(extensionSet);
	}
	
	public void setValue(String key, Object value) {
		if (!typeMap.containsKey(key))
			throw new IllegalArgumentException("Unknown key!");
		
		valueMap.put(key, value);
	}
	public void setValue(NetworkModelExtension extension, Object value) {
		setValue(extension.getKey(), value);
	}
	
	
	public void clear() {
		valueMap.clear();
		typeMap = new HashMap<String, Class<?>>(initialTypeMap);
		extensionSets.clear();
	}
	
	
	public void addModelListener(NetworkModelListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	public void addExtensionSet(NetworkModelExtensionSet extensionSet) {
		if (!getClass().equals(extensionSet.getExtendedClass()))
			throw new IllegalArgumentException("Illegal class extension!");
		if (extensionSets.contains(extensionSet))
			return;
		
		for (NetworkModelExtensionSet superExtensionSet : extensionSet.getSuperExtensionSets()) {
			addExtensionSet(superExtensionSet);
		}
		
		extensionSets.add(extensionSet);
		typeMap.putAll(extensionSet.getTypeMap());
	}
	
	public void removeModelListener(NetworkModelListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	public void removeExtensionSet(NetworkModelExtensionSet extensionSet) {
		if (!extensionSets.contains(extensionSet))
			return;
		
		for (String key : extensionSet.getTypeMap().keySet()) {
			valueMap.remove(key);
			typeMap.remove(key);
		}
		
		extensionSets.remove(extensionSet);
	}
	
	
	public void fireModelChanged() {
		synchronized (listeners) {
			for (NetworkModelListener listener : listeners) {
				listener.modelChanged(this);
			}
		}
	}
	
}