package at.rennweg.htl.netcrawler.graphics.graph;

import java.io.IOException;

import at.rennweg.htl.netcrawler.network.graph.CiscoRouter;
import at.rennweg.htl.netcrawler.network.graph.CiscoSwitch;
import at.rennweg.htl.netcrawler.network.graph.EthernetCable;
import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;
import at.rennweg.htl.netcrawler.network.graph.SerialCable;
import graphics.graph.JGraph;


public class JNetworkGraph extends JGraph {
	
	private static final long serialVersionUID = -4449144543132148622L;
	
	
	public JNetworkGraph() throws IOException {
		super();
		
		addVertexFactory(NetworkDevice.class, new DrawableImagedDeviceFactory("unknown.png"));
		addVertexFactory(CiscoRouter.class, new DrawableImagedDeviceFactory("router.png"));
		addVertexFactory(CiscoSwitch.class, new DrawableImagedDeviceFactory("switch.png"));
		
		addEdgeFactory(EthernetCable.class, new DrawableEthernetCableFactory());
		addEdgeFactory(SerialCable.class, new DrawableSerialCableFactory());
	}
	
}