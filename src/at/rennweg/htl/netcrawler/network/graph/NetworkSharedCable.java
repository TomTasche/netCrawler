package at.rennweg.htl.netcrawler.network.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import math.graph.Hyperedge;


public abstract class NetworkSharedCable<V extends NetworkDevice> extends NetworkCable<V> implements Hyperedge<V> {
	
	private Set<V> networkDevices;
	
	
	public NetworkSharedCable(V... networkDevices) {
		this.networkDevices = new HashSet<V>();
		Collections.addAll(this.networkDevices, networkDevices);
	}
	public NetworkSharedCable(Set<V> networkDevices) {
		this.networkDevices = new HashSet<V>(networkDevices);
	}
	
	
	@Override
	public Set<V> getConnectedVertices() {
		return new HashSet<V>(networkDevices);
	}
	
	@Override
	public int getVertexCount() {
		return networkDevices.size();
	}
	
}