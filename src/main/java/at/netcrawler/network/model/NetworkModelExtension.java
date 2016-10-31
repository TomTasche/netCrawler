package at.netcrawler.network.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.util.TypeToken;
import at.andiwand.library.util.collections.CollectionUtil;


public abstract class NetworkModelExtension implements Serializable {
	
	private static final long serialVersionUID = -5117690336143564492L;
	
	public static NetworkModelExtension getInstance(
			Class<? extends NetworkModelExtension> extensionClass) {
		try {
			return extensionClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Class<? extends NetworkModel> extendedModelClass;
	private Map<String, TypeToken<?>> extensionTypeMap;
	
	private boolean finalized;
	
	protected NetworkModelExtension(
			Class<? extends NetworkModel> extendedModelClass) {
		setExtendedModelClass(extendedModelClass);
		setExtensionTypeMap(new HashMap<String, TypeToken<?>>());
	}
	
	protected NetworkModelExtension(
			Class<? extends NetworkModel> extendedModelClass,
			Map<String, TypeToken<?>> extendedTypeMap) {
		setExtendedModelClass(extendedModelClass);
		setExtensionTypeMap(new HashMap<String, TypeToken<?>>(extendedTypeMap));
		
		finalize();
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (obj == null) return false;
		
		return getClass().equals(obj.getClass());
	}
	
	@Override
	public final int hashCode() {
		return getClass().hashCode();
	}
	
	public final Class<? extends NetworkModel> getExtendedModelClass() {
		checkFinalized();
		
		return extendedModelClass;
	}
	
	public final Map<String, TypeToken<?>> getExtensionTypeMap() {
		checkFinalized();
		
		return extensionTypeMap;
	}
	
	private void setExtendedModelClass(
			Class<? extends NetworkModel> extendedModelClass) {
		if (extendedModelClass == null)
			throw new IllegalArgumentException("Passing null is illigal!");
		
		this.extendedModelClass = extendedModelClass;
	}
	
	private void setExtensionTypeMap(Map<String, TypeToken<?>> extensionTypeMap) {
		this.extensionTypeMap = extensionTypeMap;
	}
	
	protected final void finalize() {
		if (finalized) return;
		
		if (!CollectionUtil.isUnmodifiableMap(extensionTypeMap))
			extensionTypeMap = Collections.unmodifiableMap(extensionTypeMap);
		
		finalized = true;
	}
	
	private void checkFinalized() throws IllegalStateException {
		if (finalized) return;
		
		throw new IllegalStateException("The extension is not finalized!");
	}
	
	private void checkNotFinalized() throws IllegalStateException {
		if (!finalized) return;
		
		throw new IllegalStateException("The extension is finalized!");
	}
	
	protected final void addExtensionType(String key, TypeToken<?> type) {
		checkNotFinalized();
		
		extensionTypeMap.put(key, type);
	}
	
	protected final void removeExtensionType(String key) {
		checkNotFinalized();
		
		extensionTypeMap.remove(key);
	}
	
}