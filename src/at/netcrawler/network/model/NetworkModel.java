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
	private final Set<NetworkModelExtension> extensions = new HashSet<NetworkModelExtension>();
	
	
	
	public NetworkModel(Map<String, Class<?>> initialTypeMap) {
		this.initialTypeMap = initialTypeMap;
		typeMap = new HashMap<String, Class<?>>(initialTypeMap);
	}
	
	
	
	@Override
	public String toString() {
		return valueMap.toString();
	}
	
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public int hashCode() {
		return valueMap.hashCode();
	}
	
	
	public Object getValue(String key) {
		return valueMap.get(key);
	}
	public Map<String, Class<?>> getTypeMap() {
		return new HashMap<String, Class<?>>(typeMap);
	}
	public Map<String, Object> getValueMap() {
		return new HashMap<String, Object>(valueMap);
	}
	public Set<NetworkModelExtension> getExtensions() {
		return new HashSet<NetworkModelExtension>(extensions);
	}
	public boolean hasExtension(NetworkModelExtension extension) {
		return extensions.contains(extension);
	}
	
	public void setValue(String key, Object value) {
		if (!typeMap.containsKey(key))
			throw new IllegalArgumentException("Unknown key!");
		
		valueMap.put(key, value);
	}
	
	
	public void clear() {
		valueMap.clear();
		typeMap = new HashMap<String, Class<?>>(initialTypeMap);
		extensions.clear();
	}
	
	
	public void addModelListener(NetworkModelListener listener) {
		listeners.add(listener);
	}
	public void addExtension(
			Class<? extends NetworkModelExtension> extensionClass) {
		try {
			NetworkModelExtension extension = extensionClass.newInstance();
			
			addExtension(extension);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public void addExtension(NetworkModelExtension extension) {
		if (!getClass().equals(extension.getExtendedModelClass()))
			throw new IllegalArgumentException("Illegal class extension!");
		if (extensions.contains(extension))
			return;
		
		for (NetworkModelExtension superExtension : extension
				.getRequiredExtensions()) {
			addExtension(superExtension);
		}
		
		extensions.add(extension);
		typeMap.putAll(extension.getExtensionTypeMap());
	}
	
	public void removeModelListener(NetworkModelListener listener) {
		listeners.remove(listener);
	}
	public void removeExtension(NetworkModelExtension extension) {
		if (!extensions.contains(extension))
			return;
		
		for (String key : extension.getExtensionTypeMap().keySet()) {
			valueMap.remove(key);
			typeMap.remove(key);
		}
		
		extensions.remove(extension);
	}
	
	
	public void fireValueChanged(String key, String value) {
		for (NetworkModelListener listener : listeners) {
			listener.valueChanged(key, value);
		}
	}
	
	public void fireExtensionAdded(NetworkModelExtension extension) {
		for (NetworkModelListener listener : listeners) {
			listener.extensionAdded(extension);
		}
	}
	
	public void fireExtensionRemoved(NetworkModelExtension extension) {
		for (NetworkModelListener listener : listeners) {
			listener.extensionRemoved(extension);
		}
	}
	
}