package at.rennweg.htl.netcrawler.network.graph;

import at.andiwand.library.math.graph.UndirectedEdge;


public class SerialLink extends PointToPointLink implements UndirectedEdge<NetworkDevice> {
	
	private static final long serialVersionUID = -3467411144191949198L;

	public SerialLink(NetworkInterface networkInterfaceA, NetworkInterface networkInterfaceB) {
		super(networkInterfaceA, networkInterfaceB);
	}
	
}