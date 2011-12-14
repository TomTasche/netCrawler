package at.netcrawler.network.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.andiwand.library.util.CollectionUtil;


public abstract class AbstractNetworkModelExtension<E extends NetworkModelExtension>
		implements NetworkModelExtension {
	
	private Class<? extends NetworkModel> extendedModelClass;
	private Set<E> requiredExtensions;
	private Map<String, Class<?>> extensionTypeMap;
	
	private boolean finalized;
	
	protected AbstractNetworkModelExtension(
			Class<? extends NetworkModel> extendedModelClass) {
		setExtendedModelClass(extendedModelClass);
		setRequiredExtensions(new HashSet<E>());
		setExtensionTypeMap(new HashMap<String, Class<?>>());
	}
	
	protected AbstractNetworkModelExtension(
			Class<? extends NetworkModel> extendedModelClass,
			Map<String, Class<?>> extendedTypeMap) {
		setExtendedModelClass(extendedModelClass);
		setRequiredExtensions(new HashSet<E>());
		setExtensionTypeMap(new HashMap<String, Class<?>>(extendedTypeMap));
		
		finalizeExtension();
	}
	
	protected AbstractNetworkModelExtension(
			Class<? extends NetworkModel> extendedModelClass,
			Set<? extends E> requiredExtensions,
			Map<String, Class<?>> extendedTypeMap) {
		setExtendedModelClass(extendedModelClass);
		setRequiredExtensions(new HashSet<E>(requiredExtensions));
		setExtensionTypeMap(new HashMap<String, Class<?>>(extendedTypeMap));
		
		finalizeExtension();
	}
	
	@Override
	public final Class<? extends NetworkModel> getExtendedModelClass() {
		checkFinalized();
		
		return extendedModelClass;
	}
	
	@Override
	public final Set<E> getRequiredExtensions() {
		checkFinalized();
		
		return requiredExtensions;
	}
	
	@Override
	public final Map<String, Class<?>> getExtensionTypeMap() {
		checkFinalized();
		
		return extensionTypeMap;
	}
	
	private void setExtendedModelClass(
			Class<? extends NetworkModel> extendedModelClass) {
		if (extendedModelClass == null) throw new IllegalArgumentException(
				"Passing null is illigal!");
		
		this.extendedModelClass = extendedModelClass;
	}
	
	private void setRequiredExtensions(Set<E> requiredExtensions) {
		this.requiredExtensions = requiredExtensions;
	}
	
	private void setExtensionTypeMap(Map<String, Class<?>> extensionTypeMap) {
		this.extensionTypeMap = extensionTypeMap;
	}
	
	protected final void finalizeExtension() {
		if (finalized) return;
		
		if (!CollectionUtil.isUnmodifiableSet(requiredExtensions)) requiredExtensions = Collections.unmodifiableSet(requiredExtensions);
		if (!CollectionUtil.isUnmodifiableMap(extensionTypeMap)) extensionTypeMap = Collections.unmodifiableMap(extensionTypeMap);
		
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
	
	protected final void addRequiredExtension(E extension) {
		checkNotFinalized();
		
		if (!extension.getExtendedModelClass().equals(extendedModelClass)) throw new IllegalArgumentException(
				"Illegal model class extension!");
		
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