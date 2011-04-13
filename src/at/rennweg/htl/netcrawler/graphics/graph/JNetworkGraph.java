package at.rennweg.htl.netcrawler.graphics.graph;

import java.io.IOException;

import at.andiwand.library.graphics.graph.JGraph;
import at.rennweg.htl.netcrawler.network.graph.CiscoRouter;
import at.rennweg.htl.netcrawler.network.graph.CiscoSwitch;
import at.rennweg.htl.netcrawler.network.graph.EthernetLink;
import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;
import at.rennweg.htl.netcrawler.network.graph.NetworkHub;
import at.rennweg.htl.netcrawler.network.graph.SerialLink;


public class JNetworkGraph extends JGraph {
	
	private static final long serialVersionUID = -4449144543132148622L;
	
	
	public JNetworkGraph() {
		super();
		
		try {
			addVertexFactory(NetworkDevice.class, new DrawableImagedDeviceFactory("unknown.png"));
			addVertexFactory(NetworkHub.class, new DrawableImagedDeviceFactory("hub.png"));
			addVertexFactory(CiscoRouter.class, new DrawableImagedDeviceFactory("router.png"));
			addVertexFactory(CiscoSwitch.class, new DrawableImagedDeviceFactory("switch.png"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		addEdgeFactory(EthernetLink.class, new DrawableEthernetLinkFactory());
		addEdgeFactory(SerialLink.class, new DrawableSerialLinkFactory());
	}
	
}