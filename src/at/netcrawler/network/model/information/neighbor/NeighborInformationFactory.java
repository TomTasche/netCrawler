package at.netcrawler.network.model.information.neighbor;

import java.util.Set;

import at.netcrawler.network.model.NetworkDeviceExtension;
import at.netcrawler.network.model.information.ModelInformationFactory;


public abstract class NeighborInformationFactory extends
		ModelInformationFactory<Set<? extends NeighborInformation>> {
	
	public NeighborInformationFactory(
			Set<NetworkDeviceExtension> requiredExtensions,
			Set<String> requiredValues) {
		super(requiredExtensions, requiredValues);
	}
	
}