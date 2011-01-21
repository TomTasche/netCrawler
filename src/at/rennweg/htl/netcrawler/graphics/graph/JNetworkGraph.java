package at.rennweg.htl.netcrawler.graphics.graph;

import at.rennweg.htl.netcrawler.network.graph.CiscoRouter;
import at.rennweg.htl.netcrawler.network.graph.CiscoSwitch;
import at.rennweg.htl.netcrawler.network.graph.EthernetCable;
import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;
import at.rennweg.htl.netcrawler.network.graph.SerialCable;
import graphics.graph.JGraph;


public class JNetworkGraph extends JGraph {
	
	private static final long serialVersionUID = -4449144543132148622L;
	
	
	public JNetworkGraph() {
		super();
		
		addVertexFactory(NetworkDevice.class, new DrawableUnknowenNetworkDeviceFactory());
		addVertexFactory(CiscoRouter.class, new DrawableCiscoRouterFactory());
		addVertexFactory(CiscoSwitch.class, new DrawableCiscoSwitchFactory());
		
		addEdgeFactory(EthernetCable.class, new DrawableEthernetCableFactory());
		addEdgeFactory(SerialCable.class, new DrawableSerialCableFactory());
	}
	
}