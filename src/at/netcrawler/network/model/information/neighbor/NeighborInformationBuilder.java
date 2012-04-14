package at.netcrawler.network.model.information.neighbor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.netcrawler.network.model.NetworkDevice;


// TODO: super class
// TODO: device manager level
public class NeighborInformationBuilder {
	
	private final List<NeighborInformationFactory> factories = new ArrayList<NeighborInformationFactory>();
	
	public NeighborInformationBuilder() {
		
	}
	
	public void addFactory(NeighborInformationFactory factory) {
		factories.add(factory);
	}
	
	public void removeFactory(NeighborInformationFactory factory) {
		factories.remove(factory);
	}
	
	public Set<? extends NeighborInformation> getIdentification(
			NetworkDevice device) {
		Set<NeighborInformation> result = new HashSet<NeighborInformation>();
		
		for (NeighborInformationFactory factory : factories) {
			Set<? extends NeighborInformation> neighborInformationSet = factory
					.build(device);
			if (neighborInformationSet != null)
				result.addAll(neighborInformationSet);
		}
		
		return result;
	}
	
}