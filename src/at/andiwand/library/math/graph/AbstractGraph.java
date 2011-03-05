package at.andiwand.library.math.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class AbstractGraph<V, E extends Edge<V>> implements Graph<V, E> {
	
	@Override
	public boolean isConnected() {
		Set<V> vertices = getVertices();
		
		if (vertices.isEmpty()) return false;
		V startVertex = vertices.iterator().next();
		
		System.out.println("start: " + startVertex);
		Set<V> unreachedVertices = new HashSet<V>(vertices);
		isConnectedImpl(startVertex, unreachedVertices);
		
		return unreachedVertices.size() == 0;
	}
	private void isConnectedImpl(V startVertex, Set<V> unreachedVertices) {
		unreachedVertices.remove(startVertex);
		
		if (unreachedVertices.isEmpty()) return;
		
		Set<V> reachableVertices = getConnectedVertices(startVertex);
		reachableVertices.retainAll(unreachedVertices);
		
		for (V vertex : reachableVertices) {
			if (startVertex.equals(vertex)) continue;
			
			isConnectedImpl(vertex, unreachedVertices);
		}
	}
	
	
	@Override
	public int getVertexCount() {
		return getVertices().size();
	}
	
	@Override
	public int getEdgeCount() {
		return getEdges().size();
	}
	
	
	@Override
	public Set<V> getConnectedVertices(V vertex) {
		Collection<E> edges = getEdges();
		Set<V> result = new HashSet<V>();
		
		for (E edge : edges) {
			Set<V> connectedVerices = edge.getConnectedVertices();
			
			result.addAll(connectedVerices);
		}
		
		return result;
	}
	
	@Override
	public List<E> getConnectedEdges(V vertex) {
		Collection<E> edges = getEdges();
		List<E> result = new ArrayList<E>();
		
		for (E edge : edges) {
			Set<V> connectedVerices = edge.getConnectedVertices();
			
			if (connectedVerices.contains(vertex)) result.add(edge);
		}
		
		return result;
	}
	
}