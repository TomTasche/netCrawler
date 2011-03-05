package at.rennweg.htl.netcrawler.network.graph;

import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.math.graph.AbstractEdge;



public abstract class NetworkLink extends AbstractEdge<NetworkDevice> {
	
	public abstract Set<NetworkInterface> getConnectedInterfaces();
	
	@Override
	public Set<NetworkDevice> getConnectedVertices() {
		Set<NetworkInterface> connectedInterfaces = getConnectedInterfaces();
		Set<NetworkDevice> result = new HashSet<NetworkDevice>();
		
		for (NetworkInterface connectedInterface : connectedInterfaces) {
			result.add(connectedInterface.parentDevice);
		}
		
		return result;
	}
	
}