package at.netcrawler.network.topology;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class HashTopology extends Topology {
	
	private class InterfaceAdapter extends TopologyDeviceAdapter {
		@Override
		public void interfaceAdded(TopologyInterface interfaze) {
			synchronized (HashTopology.this) {
				interfaces.add(interfaze);
			}
		}
		
		@Override
		public void interfaceRemoved(TopologyInterface interfaze) {
			synchronized (HashTopology.this) {
				TopologyCable cable = connectionMap.get(interfaze);
				removeEdge(cable);
				
				interfaces.remove(interfaze);
			}
		}
	}
	
	private final Set<TopologyDevice> vertices = new HashSet<TopologyDevice>();
	private final Set<TopologyInterface> interfaces = new HashSet<TopologyInterface>();
	private final Map<TopologyInterface, TopologyCable> connectionMap = new HashMap<TopologyInterface, TopologyCable>();
	private final Set<TopologyCable> edges = new HashSet<TopologyCable>();
	
	public HashTopology() {}
	
	public HashTopology(Topology topology) {
		for (TopologyDevice device : topology.getVertices()) {
			addVertex(device);
		}
		
		for (TopologyCable cable : topology.getEdges()) {
			addEdge(cable);
		}
	}
	
	@Override
	public synchronized int getVertexCount() {
		return vertices.size();
	}
	
	@Override
	public synchronized int getEdgeCount() {
		return edges.size();
	}
	
	@Override
	public synchronized Set<TopologyDevice> getVertices() {
		return new HashSet<TopologyDevice>(vertices);
	}
	
	@Override
	public synchronized Set<TopologyCable> getEdges() {
		return new HashSet<TopologyCable>(edges);
	}
	
	@Override
	public synchronized Map<TopologyInterface, TopologyCable> getConnectionMap() {
		return new HashMap<TopologyInterface, TopologyCable>(connectionMap);
	}
	
	@Override
	public synchronized Set<TopologyCable> getConnectedEdges(
			TopologyDevice vertex) {
		Set<TopologyCable> result = new HashSet<TopologyCable>();
		
		for (TopologyInterface interfaze : vertex.getInterfaces()) {
			TopologyCable cable = connectionMap.get(interfaze);
			result.add(cable);
		}
		
		return result;
	}
	
	@Override
	protected synchronized boolean addVertexImpl(TopologyDevice vertex) {
		if (vertices.contains(vertex)) return false;
		
		vertex.addListener(new InterfaceAdapter());
		interfaces.addAll(vertex.getInterfaces());
		vertices.add(vertex);
		
		return true;
	}
	
	@Override
	protected synchronized boolean addEdgeImpl(TopologyCable cable) {
		if (edges.contains(cable)) return false;
		
		for (TopologyInterface interfaze : cable.getConnectedInterfaces()) {
			if (!interfaces.contains(interfaze)) return false;
			if (connectionMap.containsKey(interfaze)) return false;
			connectionMap.put(interfaze, cable);
		}
		
		edges.add(cable);
		
		return true;
	}
	
	@Override
	protected synchronized boolean removeVertexImpl(TopologyDevice vertex) {
		if (!vertices.contains(vertex)) return false;
		
		for (TopologyInterface interfaze : vertex.getInterfaces()) {
			TopologyCable cable = connectionMap.get(interfaze);
			removeEdge(cable);
			
			interfaces.remove(interfaze);
		}
		
		vertices.remove(vertex);
		
		return true;
	}
	
	@Override
	protected synchronized boolean removeEdgeImpl(TopologyCable edge) {
		if (!edges.contains(edge)) return false;
		
		for (TopologyInterface interfaze : edge.getConnectedInterfaces()) {
			connectionMap.remove(interfaze);
		}
		
		edges.remove(edge);
		
		return true;
	}
	
}