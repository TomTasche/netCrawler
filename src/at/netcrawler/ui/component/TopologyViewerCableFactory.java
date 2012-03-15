package at.netcrawler.ui.component;

import java.util.Set;

import at.andiwand.library.component.GenericGraphViewerEdgeFactory;
import at.andiwand.library.component.GraphViewerVertex;
import at.netcrawler.network.topology.TopologyCable;


public class TopologyViewerCableFactory extends
		GenericGraphViewerEdgeFactory<TopologyCable, TopologyViewerCable> {
	
	public TopologyViewerCableFactory() {
		super(TopologyCable.class, TopologyViewerCable.class);
	}
	
	@Override
	protected TopologyViewerCable buildEdgeGeneric(TopologyCable cable,
			Set<GraphViewerVertex> devices) {
		return new TopologyViewerCable(cable, devices);
	}
	
}