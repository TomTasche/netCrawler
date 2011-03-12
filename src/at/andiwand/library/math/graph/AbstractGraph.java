package at.andiwand.library.math.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 
 * An abstract implementation of the <code>Graph</code> interface. This class
 * implements simple methods to make it easier for extended classes to implement
 * an <code>Graph</code>.
 * 
 * @author Andreas Stefl
 * 
 * @param <V> the type of the vertices.
 * @param <E> the type of the edges.
 * 
 */

//TODO: isConnected()?
public abstract class AbstractGraph<V, E extends Edge<V>> implements Graph<V, E> {
	
	/**
	 * Returns <code>true</code> if the graph is connected. <br>
	 * This method copies the list of the containing vertices and tries to reach
	 * all of them recursively.
	 * 
	 * @return <code>true</code> if the graph is connected.
	 */
	@Override
	public boolean isConnected() {
		Set<V> vertices = getVertices();
		
		if (vertices.isEmpty()) return false;
		V startVertex = vertices.iterator().next();
		
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
	
	
	/**
	 * Returns the count of the containing vertices. <br>
	 * This method simply returns <code>getVertices().size()</code>. If the size
	 * is directly known by a subclass, this method should be overwritten.
	 * 
	 * @return the count of the containing vertices.
	 */
	@Override
	public int getVertexCount() {
		Set<V> vertices = getVertices();
		
		return vertices.size();
	}
	
	/**
	 * Returns the count of the containing edges. <br>
	 * This method simply returns <code>getEdges().size()</code>. <br>
	 * If the size is directly known by a subclass, this method should be
	 * overwritten to increase the performance.
	 * 
	 * @return the count of the containing vertices.
	 */
	@Override
	public int getEdgeCount() {
		return getEdges().size();
	}
	
	
	/**
	 * Returns all connected edges of the given vertex. <br>
	 * This method iterates all containing edges and if one contains the given
	 * vertex it is stored in the result list which is mutable. <br>
	 * If a subclass provides a better data structure, this method should be
	 * overwritten to increase the performance.
	 * 
	 * @param vertex the vertex.
	 * @return all connected edges of the given vertex.
	 */
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
	
	/**
	 * Returns all connected vertices of the given edge. <br>
	 * The data is fetched by calling <code>getConnectedEdges(V)</code> with the
	 * given vertex and excluding it. Then it is returned in a mutable set. <br>
	 * If a subclass provides a better data structure, this method should be
	 * overwritten to increase the performance. <br>
	 * Note: the returned set would never contains the given vertex.
	 * 
	 * @param edge the edge.
	 * @return all connected vertices of the given edge.
	 */
	@Override
	public Set<V> getConnectedVertices(V vertex) {
		Collection<E> connectedEdges = getConnectedEdges(vertex);
		Set<V> result = new HashSet<V>();
		
		for (E edge : connectedEdges) {
			Set<V> connectedVerices = edge.getConnectedVertices();
			
			result.addAll(connectedVerices);
		}
		
		result.remove(vertex);
		
		return result;
	}
	
}