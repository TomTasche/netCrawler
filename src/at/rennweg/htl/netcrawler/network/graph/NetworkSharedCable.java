package at.rennweg.htl.netcrawler.network.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.math.graph.Hyperedge;



public abstract class NetworkSharedCable extends NetworkCable implements Hyperedge<NetworkDevice> {
	
	private Set<NetworkInterface> networkInterfaces;
	
	
	public NetworkSharedCable(NetworkInterface... networkInterfaces) {
		this.networkInterfaces = new HashSet<NetworkInterface>();
		Collections.addAll(this.networkInterfaces, networkInterfaces);
	}
	public NetworkSharedCable(Set<NetworkInterface> networkInterfaces) {
		this.networkInterfaces = new HashSet<NetworkInterface>(networkInterfaces);
	}
	
	
	@Override
	public Set<NetworkInterface> getConnectedInterfaces() {
		return new HashSet<NetworkInterface>(networkInterfaces);
	}
	
}