package at.netcrawler.network.topology.identifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;


public abstract class DeviceIdentifierFactory {
	
	private final Set<NetworkDeviceExtension> requiredExtensions;
	private final Set<String> requiredValues;
	
	public DeviceIdentifierFactory(
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
	
	public final DeviceIdentifier buildIdentifier(NetworkDevice device) {
		if (!device.getExtensions().containsAll(requiredExtensions))
			return null;
		if (!device.getValueMap().keySet().containsAll(requiredValues))
			return null;
		
		return buildIdentifierImpl(device);
	}
	
	protected abstract DeviceIdentifier buildIdentifierImpl(NetworkDevice device);
	
}