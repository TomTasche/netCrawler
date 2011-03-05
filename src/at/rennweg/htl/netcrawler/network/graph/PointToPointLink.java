package at.rennweg.htl.netcrawler.network.graph;

import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.math.graph.UndirectedEdge;


public abstract class PointToPointLink extends NetworkLink implements UndirectedEdge<NetworkDevice> {
	
	protected NetworkInterface networkInterfaceA;
	protected NetworkInterface networkInterfaceB;
	
	
	
	public PointToPointLink(NetworkInterface networkInterfaceA, NetworkInterface networkInterfaceB) {
		this.networkInterfaceA = networkInterfaceA;
		this.networkInterfaceB = networkInterfaceB;
	}
	
	
	
	@Override
	public boolean isLoop() {
		return networkInterfaceA.equals(networkInterfaceB);
	}
	
	@Override
	public NetworkDevice getVertexA() {
		return networkInterfaceA.parentDevice;
	}
	
	@Override
	public NetworkDevice getVertexB() {
		return networkInterfaceB.parentDevice;
	}
	
	
	@Override
	public Set<NetworkDevice> getConnectedVertices() {
		Set<NetworkDevice> result = new HashSet<NetworkDevice>();
		
		result.add(networkInterfaceA.parentDevice);
		result.add(networkInterfaceB.parentDevice);
		
		return result;
	}
	
	@Override
	public Set<NetworkInterface> getConnectedInterfaces() {
		Set<NetworkInterface> result = new HashSet<NetworkInterface>();
		
		result.add(networkInterfaceA);
		result.add(networkInterfaceB);
		
		return result;
	}
	
	@Override
	public int getVertexCount() {
		return VERTEX_COUNT;
	}
	
}