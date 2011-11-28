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
	
	public final Object getValue(String key) {
		return valueMap.get(key);
	}
	
	public final Map<String, Class<?>> getTypeMap() {
		return new HashMap<String, Class<?>>(typeMap);
	}
	
	public final Map<String, Object> getValueMap() {
		return new HashMap<String, Object>(valueMap);
	}
	
	public final Set<NetworkModelExtension> getExtensions() {
		return new HashSet<NetworkModelExtension>(extensions);
	}
	
	public final boolean hasExtension(NetworkModelExtension extension) {
		return extensions.contains(extension);
	}
	
	public final boolean hasExtension(
			Class<? extends NetworkModelExtension> extensionClass) {
		try {
			NetworkModelExtension extension = extensionClass.newInstance();
			
			return hasExtension(extension);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public final void setValue(String key, Object value) {
		if (!typeMap.containsKey(key)) throw new IllegalArgumentException(
				"Unknown key!");
		
		valueMap.put(
				key, value);
	}
	
	public final void clear() {
		valueMap.clear();
		typeMap = new HashMap<String, Class<?>>(initialTypeMap);
		extensions.clear();
	}
	
	public final void clearExtension(
			Class<? extends NetworkModelExtension> extensionClass) {
		try {
			NetworkModelExtension extension = extensionClass.newInstance();
			
			clearExtension(extension);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public final void clearExtension(NetworkModelExtension extension) {
		for (String extensionKey : extension.getExtensionTypeMap().keySet()) {
			valueMap.remove(extensionKey);
		}
	}
	
	public final boolean addModelListener(NetworkModelListener listener) {
		return listeners.add(listener);
	}
	
	public final boolean addExtension(
			Class<? extends NetworkModelExtension> extensionClass) {
		try {
			NetworkModelExtension extension = extensionClass.newInstance();
			
			return addExtension(extension);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public final boolean addExtension(NetworkModelExtension extension) {
		if (!getClass().equals(
				extension.getExtendedModelClass())) throw new IllegalArgumentException(
				"Illegal model class extension!");
		if (extensions.contains(extension)) return false;
		if (extension.isExtensionSupported(this)) return false;
		
		extensions.add(extension);
		typeMap.putAll(extension.getExtensionTypeMap());
		
		return true;
	}
	
	public final boolean removeModelListener(NetworkModelListener listener) {
		return listeners.remove(listener);
	}
	
	public final boolean removeExtension(NetworkModelExtension extension) {
		if (!extensions.contains(extension)) return false;
		
		for (String key : extension.getExtensionTypeMap().keySet()) {
			valueMap.remove(key);
			typeMap.remove(key);
		}
		
		return extensions.remove(extension);
	}
	
	public final void fireValueChanged(String key, String value) {
		for (NetworkModelListener listener : listeners) {
			listener.valueChanged(
					key, value);
		}
	}
	
	public final void fireExtensionAdded(NetworkModelExtension extension) {
		for (NetworkModelListener listener : listeners) {
			listener.extensionAdded(extension);
		}
	}
	
	public final void fireExtensionRemoved(NetworkModelExtension extension) {
		for (NetworkModelListener listener : listeners) {
			listener.extensionRemoved(extension);
		}
	}
	
}