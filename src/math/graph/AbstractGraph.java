package math.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class AbstractGraph<V, E extends AbstractEdge<V>> implements Graph<V, E> {
	
	@Override
	public boolean isConnected() {
		Set<V> vertices = getVertices();
		
		if (vertices.isEmpty()) return false;
		V startVertex = vertices.iterator().next();
		
		isConnectedImpl(startVertex, vertices);
		
		return vertices.size() == 0;
	}
	private void isConnectedImpl(V startVertex, Set<V> unreachedVertices) {
		unreachedVertices.remove(startVertex);
		
		if (unreachedVertices.isEmpty()) return;
		
		Set<V> reachableVertices = getConnectedVertices(startVertex);
		
		for (V vertex : reachableVertices) {
			isConnectedImpl(vertex, unreachedVertices);
		}
	}
	
	@Override
	public boolean isSimple() {
		Collection<E> edges = getEdges();
		Set<E> edgeSet = new HashSet<E>(edges);
		
		return edges.size() != edgeSet.size();
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
			Collection<V> connectedVerices = edge.getConnectedVertices();
			
			connectedVerices.remove(vertex);
			result.addAll(connectedVerices);
		}
		
		return result;
	}
	
	@Override
	public Collection<E> getConnectedEdges(V vertex) {
		Collection<E> edges = getEdges();
		List<E> result = new ArrayList<E>();
		
		for (E edge : edges) {
			Collection<V> connectedVerices = edge.getConnectedVertices();
			
			if (connectedVerices.contains(vertex)) result.add(edge);
		}
		
		return result;
	}
	
}