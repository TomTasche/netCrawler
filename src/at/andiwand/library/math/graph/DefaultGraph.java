package at.andiwand.library.math.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 
 * A simple final implementation of an listenable undirected multigraph. <br>
 * It stores vertices and edges in flat collections and implements the last
 * methods.
 * 
 * @author Andreas Stefl
 * 
 * @param <V> the type of the vertices.
 * 
 */
public class DefaultGraph<V> extends AbstractUndirectedGraph<V, DefaultEdge<V>> implements Multigraph<V, DefaultEdge<V>>, ListenableGraph<V, DefaultEdge<V>> {
	
	private Set<V> vertices;
	private List<DefaultEdge<V>> edges;
	
	private ArrayList<GraphListener<V, DefaultEdge<V>>> listeners;
	
	
	
	
	
	/**
	 * Creates a new empty listenable undirected multigraph.
	 */
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
	
	
	/**
	 * Returns a unmodifiable set of the containing vertices.
	 * 
	 * @return a unmodifiable set of the containing vertices.
	 */
	public Set<V> getVertices() {
		return Collections.unmodifiableSet(vertices);
	}
	
	/**
	 * Returns a unmodifiable list of the containing edges.
	 * 
	 * @return a unmodifiable list of the containing edges.
	 */
	public List<DefaultEdge<V>> getEdges() {
		return Collections.unmodifiableList(edges);
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
	
	/**
	 * Adds a new self-loop with the given vertex to the graph. <br>
	 * If the edge is not allowed to be added, it won't be added and
	 * <code>false</code> is returned.
	 * 
	 * @param vertex the vertex.
	 * @return <code>true</code> if the edge was added.
	 */
	public boolean addEdge(V vertex) {
		DefaultEdge<V> edge = new DefaultEdge<V>(vertex);
		
		return addEdge(edge);
	}
	
	/**
	 * Adds a new edge with the given vertices to the graph. <br>
	 * If the edge is not allowed to be added, it won't be added and
	 * <code>false</code> is returned.
	 * 
	 * @param vertexA one of the both vertices.
	 * @param vertexB one of the both vertices.
	 * @return <code>true</code> if the edge was added.
	 */
	public boolean addEdge(V vertexA, V vertexB) {
		DefaultEdge<V> edge = new DefaultEdge<V>(vertexA, vertexB);
		
		return addEdge(edge);
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
	
	/**
	 * Removes a self-loop with the given vertex from the graph. <br>
	 * If the edge cannot be removed, <code>false</code> is returned.
	 * 
	 * @param vertex the vertex.
	 * @return <code>true</code> if the edge was removed.
	 */
	public boolean removeEdge(V vertex) {
		DefaultEdge<V> edge = new DefaultEdge<V>(vertex);
		
		return removeEdge(edge);
	}
	
	/**
	 * Removes an edge with the given vertices from the graph. <br>
	 * If the edge cannot be removed, <code>false</code> is returned.
	 * 
	 * @param vertexA one of the both vertices.
	 * @param vertexB one of the both vertices.
	 * @return <code>true</code> if the edge was removed.
	 */
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
	
	/**
	 * Removes all self-loop with the given vertex from the graph. <br>
	 * If no edge is removed, <code>false</code> is returned.
	 * 
	 * @param vertex the vertex.
	 * @return <code>true</code> if one or more edges was removed.
	 */
	public boolean removeAllEdges(V vertex) {
		DefaultEdge<V> edge = new DefaultEdge<V>(vertex);
		
		return removeAllEdges(edge);
	}
	
	/**
	 * Removes all edges with the given vertices from the graph. <br>
	 * If no edge is removed, <code>false</code> is returned.
	 * 
	 * @param vertexA one of the both vertices.
	 * @param vertexB one of the both vertices.
	 * @return <code>true</code> if one or more edges was removed.
	 */
	public boolean removeAllEdges(V vertexA, V vertexB) {
		DefaultEdge<V> edge = new DefaultEdge<V>(vertexA, vertexB);
		
		return removeAllEdges(edge);
	}
	
	
	
	public void addListener(GraphListener<V, DefaultEdge<V>> listener) {
		listeners.add(listener);
	}
	
	public void removeListener(GraphListener<V, DefaultEdge<V>> listener) {
		listeners.remove(listener);
	}
	
}