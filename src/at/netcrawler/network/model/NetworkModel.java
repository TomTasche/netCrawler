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
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof NetworkModel)) return false;
		NetworkModel model = (NetworkModel) obj;
		
		return valueMap.equals(model.valueMap);
	}
	
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
		NetworkModelExtension extension = NetworkModelExtension.getInstance(extensionClass);
		return hasExtension(extension);
	}
	
	public final boolean isExtensionSupported(NetworkModelExtension extension) {
		if (!getClass().equals(extension.getExtendedModelClass())) return false;
		if (!extensions.containsAll(extension.getRequiredExtensions())) return false;
		
		return true;
	}
	
	public final boolean isExtensionSupported(
			Class<? extends NetworkModelExtension> extensionClass) {
		NetworkModelExtension extension = NetworkModelExtension.getInstance(extensionClass);
		return isExtensionSupported(extension);
	}
	
	public final void setValue(String key, Object value) {
		if (!typeMap.containsKey(key)) throw new IllegalArgumentException(
				"Unknown key!");
		Object oldValue = valueMap.put(key, value);
		
		if (value == oldValue) return;
		if ((value == null) || (value.equals(oldValue))) return;
		fireValueChanged(key, value, oldValue);
	}
	
	public final void clear() {
		valueMap.clear();
		typeMap = new HashMap<String, Class<?>>(initialTypeMap);
		extensions.clear();
	}
	
	public final void clearExtension(
			Class<? extends NetworkModelExtension> extensionClass) {
		NetworkModelExtension extension = NetworkModelExtension.getInstance(extensionClass);
		clearExtension(extension);
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
		NetworkModelExtension extension = NetworkModelExtension.getInstance(extensionClass);
		return addExtension(extension);
	}
	
	public final boolean addExtension(NetworkModelExtension extension) {
		if (!getClass().equals(extension.getExtendedModelClass())) throw new IllegalArgumentException(
				"Illegal model class extension!");
		if (extensions.contains(extension)) return false;
		if (!isExtensionSupported(extension)) return false;
		
		extensions.add(extension);
		typeMap.putAll(extension.getExtensionTypeMap());
		
		fireExtensionAdded(extension);
		return true;
	}
	
	public final boolean removeModelListener(NetworkModelListener listener) {
		return listeners.remove(listener);
	}
	
	public final boolean removeExtension(
			Class<? extends NetworkModelExtension> extensionClass) {
		NetworkModelExtension extension = NetworkModelExtension.getInstance(extensionClass);
		return removeExtension(extension);
	}
	
	public final boolean removeExtension(NetworkModelExtension extension) {
		if (!extensions.contains(extension)) return false;
		
		for (String key : extension.getExtensionTypeMap().keySet()) {
			valueMap.remove(key);
			typeMap.remove(key);
		}
		
		if (!extensions.remove(extension)) return false;
		
		fireExtensionRemoved(extension);
		return true;
	}
	
	private final void fireValueChanged(String key, Object value,
			Object oldValue) {
		for (NetworkModelListener listener : listeners) {
			listener.valueChanged(key, value, oldValue);
		}
	}
	
	private final void fireExtensionAdded(NetworkModelExtension extension) {
		for (NetworkModelListener listener : listeners) {
			listener.extensionAdded(extension);
		}
	}
	
	private final void fireExtensionRemoved(NetworkModelExtension extension) {
		for (NetworkModelListener listener : listeners) {
			listener.extensionRemoved(extension);
		}
	}
	
}