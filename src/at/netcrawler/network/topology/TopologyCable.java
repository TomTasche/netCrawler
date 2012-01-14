package at.netcrawler.network.topology;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.math.graph.AbstractHyperedge;
import at.netcrawler.network.model.NetworkCable;


public class TopologyCable extends AbstractHyperedge<TopologyDevice> {
	
	private final NetworkCable networkCable;
	
	private final Set<TopologyInterface> connectedInterfaces;
	
	public TopologyCable(NetworkCable networkCable,
			Set<TopologyInterface> connectedInterfaces) {
		this.networkCable = networkCable;
		this.connectedInterfaces = Collections.unmodifiableSet(new HashSet<TopologyInterface>(
				connectedInterfaces));
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof TopologyCable)) return false;
		TopologyCable cable = (TopologyCable) obj;
		
		return connectedInterfaces.equals(cable.connectedInterfaces);
	}
	
	@Override
	public int hashCode() {
		return connectedInterfaces.hashCode();
	}
	
	@Override
	public Set<TopologyDevice> getVertices() {
		Set<TopologyDevice> result = new HashSet<TopologyDevice>();
		
		for (TopologyInterface connectedInterface : connectedInterfaces) {
			result.add(connectedInterface.getDevice());
		}
		
		return result;
	}
	
	public NetworkCable getNetworkCable() {
		return networkCable;
	}
	
	public int getConnectionCount() {
		return connectedInterfaces.size();
	}
	
	public Set<TopologyInterface> getConnectedInterfaces() {
		return connectedInterfaces;
	}
	
}