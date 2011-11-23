package at.netcrawler.network.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public abstract class AbstractNetworkModelExtension<E extends NetworkModelExtension>
		implements NetworkModelExtension {
	
	private final Class<? extends NetworkModel> extendedModelClass;
	private Set<E> requiredExtensions;
	private Map<String, Class<?>> extensionTypeMap;
	
	private boolean finalized;
	
	
	protected AbstractNetworkModelExtension(
			Class<? extends NetworkModel> extendedModelClass) {
		if (extendedModelClass == null)
			throw new IllegalArgumentException("Passing null is illigal!");
		
		this.extendedModelClass = extendedModelClass;
		requiredExtensions = new HashSet<E>();
		extensionTypeMap = new HashMap<String, Class<?>>();
	}
	protected AbstractNetworkModelExtension(
			Class<? extends NetworkModel> extendedModelClass,
			Map<String, Class<?>> extendedTypeMap) {
		this(extendedModelClass);
		
		this.requiredExtensions = new HashSet<E>();
		this.extensionTypeMap = new HashMap<String, Class<?>>(extendedTypeMap);
		
		finalizeExtension();
	}
	protected AbstractNetworkModelExtension(
			Class<? extends NetworkModel> extendedModelClass,
			Set<E> requiredExtensions,
			Map<String, Class<?>> extendedTypeMap) {
		this(extendedModelClass);
		
		this.requiredExtensions = new HashSet<E>(requiredExtensions);
		this.extensionTypeMap = new HashMap<String, Class<?>>(extendedTypeMap);
		
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
	
	
	protected final void finalizeExtension() {
		if (finalized) return;
		
		requiredExtensions = Collections.unmodifiableSet(requiredExtensions);
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
	
	
	protected final void addRequiredExtension(E extension) {
		checkNotFinalized();
		
		requiredExtensions.add(extension);
	}
	
	protected final void addExtensionType(String key, Class<?> type) {
		checkNotFinalized();
		
		extensionTypeMap.put(key, type);
	}
	
	protected final void removeRequiredExtension(E extension) {
		checkNotFinalized();
		
		requiredExtensions.remove(extension);
	}
	
	protected final void removeExtensionType(String key) {
		checkNotFinalized();
		
		extensionTypeMap.remove(key);
	}
	
}