package at.rennweg.htl.netcrawler.graphics.graph;

import at.rennweg.htl.netcrawler.math.graph.CiscoRouter;
import at.rennweg.htl.netcrawler.math.graph.EthernetCable;
import at.rennweg.htl.netcrawler.math.graph.NetworkDevice;
import at.rennweg.htl.netcrawler.math.graph.SerialCable;
import graphics.graph.JGraph;


public class JNetworkGraph extends JGraph {
	
	private static final long serialVersionUID = -4449144543132148622L;
	
	
	public JNetworkGraph() {
		super();
		
		addVertexFactory(NetworkDevice.class, new DrawableUnknowenNetworkDeviceFactory());
		addVertexFactory(CiscoRouter.class, new DrawableCiscoRouterFactory());
		
		addEdgeFactory(EthernetCable.class, new DrawableEthernetCableFactory());
		addEdgeFactory(SerialCable.class, new DrawableSerialCableFactory());
	}
	
}