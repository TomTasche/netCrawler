package at.rennweg.htl.netcrawler.network.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.math.graph.Hyperedge;


public abstract class NetworkSharedLink extends NetworkLink implements Hyperedge<NetworkDevice> {
	
	private static final long serialVersionUID = 7132924733359489405L;
	
	
	private Set<NetworkInterface> networkInterfaces;
	
	public NetworkSharedLink(NetworkInterface... networkInterfaces) {
		this.networkInterfaces = new HashSet<NetworkInterface>();
		Collections.addAll(this.networkInterfaces, networkInterfaces);
	}
	public NetworkSharedLink(Set<NetworkInterface> networkInterfaces) {
		this.networkInterfaces = new HashSet<NetworkInterface>(networkInterfaces);
	}
	
	@Override
	public Set<NetworkInterface> getConnectedInterfaces() {
		return new HashSet<NetworkInterface>(networkInterfaces);
	}
	
}