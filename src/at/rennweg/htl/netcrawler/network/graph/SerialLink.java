package at.rennweg.htl.netcrawler.network.graph;

import at.andiwand.library.math.graph.UndirectedEdge;


public class SerialLink extends PointToPointLink implements UndirectedEdge<NetworkDevice> {
	
	public SerialLink(NetworkInterface networkInterfaceA, NetworkInterface networkInterfaceB) {
		super(networkInterfaceA, networkInterfaceB);
	}
	
}