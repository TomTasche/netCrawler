package at.netcrawler.network.model.information.neighbor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.util.collections.CollectionUtil;
import at.netcrawler.network.model.CDPNeighbor;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;
import at.netcrawler.network.model.extension.CiscoDeviceExtension;


public class CDPNeighborInformationFactory extends NeighborInformationFactory {
	
	public static final Set<NetworkDeviceExtension> REQUIRED_EXTENSIONS = Collections
			.unmodifiableSet(CollectionUtil
					.arrayToHashSet(new NetworkDeviceExtension[] {CiscoDeviceExtension.EXTENSION}));
	public static final Set<String> REQUIRED_VALUES = Collections
			.unmodifiableSet(CollectionUtil
					.arrayToHashSet(new String[] {CiscoDeviceExtension.CDP_NEIGHBORS}));
	
	public CDPNeighborInformationFactory() {
		super(REQUIRED_EXTENSIONS, REQUIRED_VALUES);
	}
	
	@Override
	protected Set<CDPNeighborInformation> buildImpl(NetworkDevice device) {
		Set<CDPNeighborInformation> result = new HashSet<CDPNeighborInformation>();
		Set<CDPNeighbor> neighbors = device
				.getValue(CiscoDeviceExtension.CDP_NEIGHBORS);
		
		for (CDPNeighbor neighbor : neighbors) {
			result.add(new CDPNeighborInformation(neighbor));
		}
		
		return result;
	}
}