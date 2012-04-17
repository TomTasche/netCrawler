package at.netcrawler.network.model.information.neighbor;

import java.util.Set;

import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;


public abstract class DetailedNeighborInforationFactory extends
		NeighborInformationFactory {
	
	public DetailedNeighborInforationFactory(
			Set<NetworkDeviceExtension> requiredExtensions,
			Set<String> requiredValues) {
		super(requiredExtensions, requiredValues);
	}
	
	@Override
	protected abstract Set<? extends DetailedNeighborInformation> buildImpl(
			NetworkDevice device);
	
}