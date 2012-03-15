package at.netcrawler.ui.component;

import at.andiwand.library.component.GraphViewer;
import at.andiwand.library.math.graph.Edge;
import at.andiwand.library.math.graph.Graph;
import at.netcrawler.network.topology.Topology;


public class TopologyViewer extends GraphViewer {
	
	private static final long serialVersionUID = 4316716282457694935L;
	
	public TopologyViewer() {
		addVertexFactory(new TopologyViewerDeviceFactory());
		addEdgeFactory(new TopologyViewerCableFactory());
	}
	
	public TopologyViewer(Topology topology) {
		this();
		
		setModel(topology);
	}
	
	@Override
	public void setModel(Graph<? extends Object, ? extends Edge> graph) {
		if (!(graph instanceof Topology))
			throw new IllegalArgumentException(
					"An Topology object must be given!");
		super.setModel(graph);
	}
	
}