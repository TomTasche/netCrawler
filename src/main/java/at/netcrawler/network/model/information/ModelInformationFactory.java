package at.netcrawler.network.model.information;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;


public abstract class ModelInformationFactory<T> {
	
	private final Set<NetworkDeviceExtension> requiredExtensions;
	private final Set<String> requiredValues;
	
	public ModelInformationFactory(
			Set<NetworkDeviceExtension> requiredExtensions,
			Set<String> requiredValues) {
		this.requiredExtensions = Collections
				.unmodifiableSet(new HashSet<NetworkDeviceExtension>(
						requiredExtensions));
		this.requiredValues = Collections.unmodifiableSet(new HashSet<String>(
				requiredValues));
	}
	
	public final Set<NetworkDeviceExtension> getRequiredExtensions() {
		return requiredExtensions;
	}
	
	public final Set<String> getRequiredValues() {
		return requiredValues;
	}
	
	public final T build(NetworkDevice device) {
		if (!device.getExtensions().containsAll(requiredExtensions))
			return null;
		if (!device.getValueMap().keySet().containsAll(requiredValues))
			return null;
		
		return buildImpl(device);
	}
	
	protected abstract T buildImpl(NetworkDevice device);
	
}