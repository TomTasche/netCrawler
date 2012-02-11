package at.netcrawler.network.model;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class NetworkModel implements Serializable {
	
	private static final long serialVersionUID = -5895614487866391126L;
	
	private transient final List<NetworkModelListener> listeners = new ArrayList<NetworkModelListener>();
	
	private transient final Map<String, Type> initialTypeMap;
	private Map<String, Type> typeMap;
	private final Map<String, Object> valueMap = new HashMap<String, Object>();
	private final Set<NetworkModelExtension> extensions = new HashSet<NetworkModelExtension>();
	
	public NetworkModel(Map<String, Type> initialTypeMap) {
		this.initialTypeMap = initialTypeMap;
		typeMap = new HashMap<String, Type>(initialTypeMap);
	}
	
	@Override
	public String toString() {
		return valueMap.toString();
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
	
	public final Map<String, Type> getTypeMap() {
		return new HashMap<String, Type>(typeMap);
	}
	
	public final Map<String, Object> getValueMap() {
		return new HashMap<String, Object>(valueMap);
	}
	
	public final Set<NetworkModelExtension> getExtensions() {
		return new HashSet<NetworkModelExtension>(extensions);
	}
	
	public final boolean isExtensionSupported(NetworkModelExtension extension) {
		if (!getClass().equals(extension.getExtendedModelClass()))
			return false;
		
		return true;
	}
	
	public final boolean isExtensionSupported(
			Class<? extends NetworkModelExtension> extensionClass) {
		NetworkModelExtension extension = NetworkModelExtension.getInstance(extensionClass);
		return isExtensionSupported(extension);
	}
	
	public final void setValue(String key, Object value) {
		if (!typeMap.containsKey(key))
			throw new IllegalArgumentException("Unknown key!");
		// TODO: fix
//		Type type = typeMap.get(key);
//		if (!type.equals(value.getClass()))
//			throw new IllegalArgumentException("Illegal type!");
		Object oldValue = valueMap.put(key, value);
		
		if (value == oldValue) return;
		if ((value == null) || (value.equals(oldValue))) return;
		fireValueChanged(key, value, oldValue);
	}
	
	public final boolean containsExtension(NetworkModelExtension extension) {
		return extensions.contains(extension);
	}
	
	public final boolean containsExtension(
			Class<? extends NetworkModelExtension> extensionClass) {
		NetworkModelExtension extension = NetworkModelExtension.getInstance(extensionClass);
		return containsExtension(extension);
	}
	
	public final void clear() {
		valueMap.clear();
		typeMap = new HashMap<String, Type>(initialTypeMap);
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
	
	public final void addListener(NetworkModelListener listener) {
		listeners.add(listener);
	}
	
	public final boolean addExtension(
			Class<? extends NetworkModelExtension> extensionClass) {
		NetworkModelExtension extension = NetworkModelExtension.getInstance(extensionClass);
		return addExtension(extension);
	}
	
	public final boolean addExtension(NetworkModelExtension extension) {
		if (!getClass().equals(extension.getExtendedModelClass()))
			throw new IllegalArgumentException("Illegal model class extension!");
		if (extensions.contains(extension)) return false;
		if (!isExtensionSupported(extension)) return false;
		
		extensions.add(extension);
		typeMap.putAll(extension.getExtensionTypeMap());
		
		fireExtensionAdded(extension);
		return true;
	}
	
	public final void removeListener(NetworkModelListener listener) {
		listeners.remove(listener);
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