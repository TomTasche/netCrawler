package at.andiwand.library.math.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DefaultGraph<V> extends AbstractUndirectedGraph<V, DefaultEdge<V>> implements Multigraph<V, DefaultEdge<V>>, ListenableGraph<V, DefaultEdge<V>> {
	
	private Set<V> vertices;
	private List<DefaultEdge<V>> edges;
	
	private ArrayList<GraphListener<V, DefaultEdge<V>>> listeners;
	
	
	
	
	public DefaultGraph() {
		vertices = new HashSet<V>();
		edges = new ArrayList<DefaultEdge<V>>();
		
		listeners = new ArrayList<GraphListener<V,DefaultEdge<V>>>();
	}
	
	
	
	
	public int getEdgeCount(DefaultEdge<V> edge) {
		int result = 0;
		
		for (DefaultEdge<V> e : edges) {
			if (e.equals(edge)) result++;
		}
		
		return result;
	}
	
	public Set<V> getVertices() {
		return Collections.unmodifiableSet(vertices);
	}
	public List<DefaultEdge<V>> getEdges() {
		return Collections.unmodifiableList(edges);
	}
	
	public Set<V> getConnectedVertices(DefaultEdge<V> edge) {
		return edge.getConnectedVertices();
	}
	
	
	
	public boolean addVertex(V vertex) {
		boolean result = vertices.add(vertex);
		
		if (result) {
			for (GraphListener<V, DefaultEdge<V>> listener : listeners) {
				listener.vertexAdded(vertex);
			}
		}
		
		return result;
	}
	public boolean addEdge(DefaultEdge<V> edge) {
		if (!vertices.contains(edge.getVertexA())) return false;
		if (!vertices.contains(edge.getVertexB())) return false;
		
		boolean result = edges.add(edge);
		
		if (result) {
			for (GraphListener<V, DefaultEdge<V>> listener : listeners) {
				listener.edgeAdded(edge);
			}
		}
		
		return result;
	}
	public boolean addEdge(V vertexA, V vertexB) {
		DefaultEdge<V> edge = new DefaultEdge<V>(vertexA, vertexB);
		
		return addEdge(edge);
	}
	
	public void addListener(GraphListener<V, DefaultEdge<V>> listener) {
		listeners.add(listener);
	}
	
	
	public boolean removeVertex(V vertex) {
		boolean result = vertices.remove(vertex);
		
		for (GraphListener<V, DefaultEdge<V>> listener : listeners) {
			listener.vertexRemoved(vertex);
		}
		
		return result;
	}
	public boolean removeEdge(DefaultEdge<V> edge) {
		boolean result = edges.remove(edge);
		
		if (result) {
			for (GraphListener<V, DefaultEdge<V>> listener : listeners) {
				listener.edgeRemoved(edge);
			}
		}
		
		return result;
	}
	public boolean removeEdge(V vertexA, V vertexB) {
		DefaultEdge<V> edge = new DefaultEdge<V>(vertexA, vertexB);
		
		return removeEdge(edge);
	}
	public boolean removeAllEdges(DefaultEdge<V> edge) {
		boolean result = false;
		
		List<DefaultEdge<V>> edgesCopy = getEdges();
		for (DefaultEdge<V> edgeItem : edgesCopy) {
			if (edgeItem.equals(edge)) result |= removeEdge(edgeItem);
		}
		
		return result;
	}
	public boolean removeAllEdges(V vertexA, V vertexB) {
		DefaultEdge<V> edge = new DefaultEdge<V>(vertexA, vertexB);
		
		return removeAllEdges(edge);
	}
	
	public void removeListener(GraphListener<V, DefaultEdge<V>> listener) {
		listeners.remove(listener);
	}
	
}