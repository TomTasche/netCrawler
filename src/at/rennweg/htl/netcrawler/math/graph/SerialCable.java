package at.rennweg.htl.netcrawler.math.graph;

import java.util.HashSet;
import java.util.Set;

import math.graph.UndirectedEdge;


public class SerialCable<V extends NetworkDevice> extends NetworkCable<V> implements UndirectedEdge<V> {
	
	private V networkDeviceA;
	private V networkDeviceB;
	
	
	
	public SerialCable(V networkDeviceA, V networkDeviceB) {
		this.networkDeviceA = networkDeviceA;
		this.networkDeviceB = networkDeviceB;
	}
	
	
	
	@Override
	public boolean isLoop() {
		return networkDeviceA.equals(networkDeviceB);
	}
	
	@Override
	public V getVertexA() {
		return networkDeviceA;
	}
	
	@Override
	public V getVertexB() {
		return networkDeviceB;
	}
	
	
	@Override
	public Set<V> getConnectedVertices() {
		Set<V> result = new HashSet<V>();
		
		result.add(networkDeviceA);
		result.add(networkDeviceB);
		
		return result;
	}
	
	@Override
	public int getVertexCount() {
		return 2;
	}
	
}