package at.netcrawler.network.model.information.identifier;

import java.util.Set;

import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;
import at.netcrawler.network.model.information.ModelInformationFactory;


public abstract class DeviceIdentifierFactory extends
		ModelInformationFactory<DeviceIdentifier> {
	
	public DeviceIdentifierFactory(
			Set<NetworkDeviceExtension> requiredExtensions,
			Set<String> requiredValues) {
		super(requiredExtensions, requiredValues);
	}
	
	protected abstract DeviceIdentifier buildImpl(NetworkDevice device);
	
}