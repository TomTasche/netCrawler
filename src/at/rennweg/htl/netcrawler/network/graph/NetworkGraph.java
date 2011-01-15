package at.rennweg.htl.netcrawler.network.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import math.graph.AbstractGraph;
import math.graph.GraphListener;
import math.graph.ListenableGraph;


// TODO accelerate
public class NetworkGraph<V extends NetworkDevice, E extends NetworkCable<V>> extends AbstractGraph<V, E> implements ListenableGraph<V, E> {
	
	private Set<V> vertices;
	private List<E> cables;
	
	private List<GraphListener<V, E>> listeners;
	
	
	
	
	public NetworkGraph() {
		vertices = new HashSet<V>();
		cables = new ArrayList<E>();
		
		listeners = new ArrayList<GraphListener<V,E>>();
	}
	
	
	
	
	public int getEdgeCount(E edge) {
		int result = 0;
		
		for (E e : cables) {
			if (e.equals(edge)) result++;
		}
		
		return result;
	}
	
	public int getCableCount(E cable) {
		return getCableCount(cable);
	}
	
	
	@Override
	public Set<V> getVertices() {
		return new HashSet<V>(vertices);
	}
	
	@Override
	public List<E> getEdges() {
		return new ArrayList<E>(cables);
	}
	
	public List<E> getCables() {
		return getEdges();
	}
	
	
	@Override
	public Set<V> getConnectedVertices(E cable) {
		return cable.getConnectedVertices();
	}
	
	public Set<E> getConnectedCables(V networkDevice) {
		Set<E> result = new HashSet<E>();
		
		for (E cable : cables) {
			if (cable.getConnectedVertices().contains(networkDevice))
				result.add(cable);
		}
		
		return result;
	}
	
	
	/*public Set<NetworkInterface> getAviableInterfaces(V networkDevice) {
		Set<NetworkInterface> result = new HashSet<NetworkInterface>();
		Set<E> cables = getConnectedCables(networkDevice);
		
		for (E cable : cables) {
			result.add(cable.getConnectedInterface(networkDevice));
		}
		
		return result;
	}*/
	
	
	
	
	@Override
	public boolean addVertex(V vertex) {
		boolean result = vertices.add(vertex);
		
		if (result) {
			for (GraphListener<V, E> listener : listeners) {
				listener.vertexAdded(vertex);
			}
		}
		
		return result;
	}
	
	@Override
	public boolean addEdge(E edge) {
		if (!vertices.containsAll(edge.getConnectedVertices())) return false;
		
		boolean result = cables.add(edge);
		
		if (result) {
			for (GraphListener<V, E> listener : listeners) {
				listener.edgeAdded(edge);
			}
		}
		
		return result;
	}
	
	public boolean addCable(E cable) {
		return addEdge(cable);
	}
	
	
	@Override
	public void addListener(GraphListener<V, E> listener) {
		listeners.add(listener);
	}
	
	
	
	@Override
	public boolean removeVertex(V vertex) {
		boolean result = vertices.remove(vertex);
		
		for (GraphListener<V, E> listener : listeners) {
			listener.vertexRemoved(vertex);
		}
		
		return result;
	}
	
	@Override
	public boolean removeEdge(E edge) {
		boolean result = cables.remove(edge);
		
		if (result) {
			for (GraphListener<V, E> listener : listeners) {
				listener.edgeRemoved(edge);
			}
		}
		
		return result;
	}
	
	@Override
	public boolean removeAllEdges(E edge) {
		boolean result = false;
		
		List<E> edgesCopy = getEdges();
		for (E edgeItem : edgesCopy) {
			if (edgeItem.equals(edge)) result |= removeEdge(edgeItem);
		}
		
		return result;
	}
	
	
	@Override
	public void removeListener(GraphListener<V, E> listener) {
		listeners.remove(listener);
	}
	
}