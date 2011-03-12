package at.andiwand.library.math.graph;

import java.util.Set;


/**
 * 
 * The root of all hypergraphs.
 * 
 * @author Andreas Stefl
 * 
 * @param <V> the type of the vertices.
 * @param <E> the type of the edges.
 * 
 */
public interface Hypergraph<V, E extends Hyperedge<V>> extends Graph<V, E> {
	
	/**
	 * Returns a set of the containing hyperedges.
	 * 
	 * @return a set of the containing hyperedges.
	 */
	public Set<E> getEdges();
	
	/**
	 * Returns all connected hyperedges of the given vertex.
	 * 
	 * @param vertex the vertex.
	 * @return all connected hyperedges of the given vertex.
	 */
	public Set<E> getConnectedEdges(V vertex);
	
}