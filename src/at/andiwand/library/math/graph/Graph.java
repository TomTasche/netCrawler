package at.andiwand.library.math.graph;

import java.util.Collection;
import java.util.Set;


/**
 * 
 * The root of all graphs. <br>
 * 
 * @author Andreas Stefl
 * 
 * @param <V> the type of the vertices.
 * @param <E> the type of the edges.
 * 
 */

//TODO: isConnected()? isSimple()?
public interface Graph<V, E extends Edge<V>> {
	
	/**
	 * Returns a string that represents the graph.
	 * 
	 * @return a string that represents the graph.
	 */
	public String toString();
	
	/**
	 * Returns <code>true</code> if the given object is equal to the graph.
	 * Normally the equality is defined by the containing vertices and edges.
	 * 
	 * @param obj the object to compare.
	 * @return <code>true</code> if the given object is equal to the graph.
	 */
	public boolean equals(Object obj);
	
	/**
	 * Returns the hash code of the edge.
	 * 
	 * @return the hash code of the edge.
	 */
	public int hashCode();
	
	
	
	/**
	 * Returns <code>true</code> if the graph is connected.
	 * 
	 * @return <code>true</code> if the graph is connected.
	 */
	public boolean isConnected();
	
	/**
	 * Returns <code>true</code> if the graph is simple.
	 * 
	 * @return <code>true</code> if the graph is simple.
	 */
	public boolean isSimple();
	
	
	/**
	 * Returns the count of the containing vertices.
	 * 
	 * @return the count of the containing vertices.
	 */
	public int getVertexCount();
	
	/**
	 * Returns the count of the containing edges.
	 * 
	 * @return the count of the containing edges.
	 */
	public int getEdgeCount();
	
	
	/**
	 * Returns a set of the containing vertices.
	 * 
	 * @return a set of the containing vertices.
	 */
	public Set<V> getVertices();
	
	/**
	 * Returns a collection of the containing edges.
	 * 
	 * @return a collection of the containing edges.
	 */
	public Collection<E> getEdges();
	
	
	/**
	 * Returns all connected edges of the given vertex.
	 * 
	 * @param vertex the vertex.
	 * @return all connected edges of the given vertex.
	 */
	public Collection<E> getConnectedEdges(V vertex);
	
	/**
	 * Returns all connected vertices of the given vertex.
	 * 
	 * @param vertex the vertex.
	 * @return all connected vertices of the given vertex.
	 */
	public Set<V> getConnectedVertices(V vertex);
	
	
	
	/**
	 * Adds the given vertex to the graph. If the vertex is already in the
	 * graph, it won't be added and <code>false</code> is returned.
	 * 
	 * @param vertex the vertex.
	 * @return <code>true</code> if the vertex was added.
	 */
	public boolean addVertex(V vertex);
	
	/**
	 * Adds the given edge to the graph. If the edge is not allowed to be added,
	 * it won't be added and <code>false</code> is returned.
	 * 
	 * @param edge the v.
	 * @return <code>true</code> if the edge was added.
	 */
	public boolean addEdge(E edge);
	
	
	/**
	 * Removes the given vertex from the graph. If the vertex cannot be removed,
	 * <code>false</code> is returned.
	 * 
	 * @param vertex the vertex.
	 * @return <code>true</code> if the vertex was removed.
	 */
	public boolean removeVertex(V vertex);
	
	/**
	 * Removes the given edge from the graph. If the edge cannot be removed,
	 * <code>false</code> is returned.
	 * 
	 * @param edge the edge.
	 * @return <code>true</code> if the edge was removed.
	 */
	public boolean removeEdge(E edge);
	
	/**
	 * Removes all equal edges from the graph. If no edge is removed,
	 * <code>false</code> is returned.
	 * 
	 * @param edge the vertex.
	 * @return <code>true</code> if one or more edges was removed.
	 */
	public boolean removeAllEdges(E edge);
	
}