package at.netcrawler.network.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.andiwand.library.util.CollectionUtil;


public abstract class NetworkModelExtension {
	
	public static NetworkModelExtension getInstance(
			Class<? extends NetworkModelExtension> extensionClass) {
		try {
			return extensionClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Class<? extends NetworkModel> extendedModelClass;
	private Set<NetworkModelExtension> requiredExtensions;
	private Map<String, Class<?>> extensionTypeMap;
	
	private boolean finalized;
	
	protected NetworkModelExtension(
			Class<? extends NetworkModel> extendedModelClass) {
		setExtendedModelClass(extendedModelClass);
		setRequiredExtensions(new HashSet<NetworkModelExtension>());
		setExtensionTypeMap(new HashMap<String, Class<?>>());
	}
	
	protected NetworkModelExtension(
			Class<? extends NetworkModel> extendedModelClass,
			Map<String, Class<?>> extendedTypeMap) {
		setExtendedModelClass(extendedModelClass);
		setRequiredExtensions(new HashSet<NetworkModelExtension>());
		setExtensionTypeMap(new HashMap<String, Class<?>>(extendedTypeMap));
		
		finalizeExtension();
	}
	
	protected NetworkModelExtension(
			Class<? extends NetworkModel> extendedModelClass,
			Set<? extends NetworkModelExtension> requiredExtensions,
			Map<String, Class<?>> extendedTypeMap) {
		setExtendedModelClass(extendedModelClass);
		setRequiredExtensions(new HashSet<NetworkModelExtension>(
				requiredExtensions));
		setExtensionTypeMap(new HashMap<String, Class<?>>(extendedTypeMap));
		
		finalizeExtension();
	}
	
	@Override
	public final int hashCode() {
		return getClass().hashCode();
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (obj == null) return false;
		
		return getClass().equals(obj.getClass());
	}
	
	public final Class<? extends NetworkModel> getExtendedModelClass() {
		checkFinalized();
		
		return extendedModelClass;
	}
	
	public final Set<NetworkModelExtension> getRequiredExtensions() {
		checkFinalized();
		
		return requiredExtensions;
	}
	
	public final Map<String, Class<?>> getExtensionTypeMap() {
		checkFinalized();
		
		return extensionTypeMap;
	}
	
	private void setExtendedModelClass(
			Class<? extends NetworkModel> extendedModelClass) {
		if (extendedModelClass == null)
			throw new IllegalArgumentException("Passing null is illigal!");
		
		this.extendedModelClass = extendedModelClass;
	}
	
	private void setRequiredExtensions(
			Set<NetworkModelExtension> requiredExtensions) {
		this.requiredExtensions = requiredExtensions;
	}
	
	private void setExtensionTypeMap(Map<String, Class<?>> extensionTypeMap) {
		this.extensionTypeMap = extensionTypeMap;
	}
	
	protected final void finalizeExtension() {
		if (finalized) return;
		
		if (!CollectionUtil.isUnmodifiableSet(requiredExtensions))
			requiredExtensions = Collections.unmodifiableSet(requiredExtensions);
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
	
	protected final void addRequiredExtension(NetworkModelExtension extension) {
		checkNotFinalized();
		
		if (!extension.getExtendedModelClass().equals(extendedModelClass))
			throw new IllegalArgumentException("Illegal model class extension!");
		
		requiredExtensions.add(extension);
	}
	
	protected final void addExtensionType(String key, Class<?> type) {
		checkNotFinalized();
		
		extensionTypeMap.put(key, type);
	}
	
	protected final void removeRequiredExtension(NetworkModelExtension extension) {
		checkNotFinalized();
		
		requiredExtensions.remove(extension);
	}
	
	protected final void removeExtensionType(String key) {
		checkNotFinalized();
		
		extensionTypeMap.remove(key);
	}
	
}