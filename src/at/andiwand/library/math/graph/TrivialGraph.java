package at.andiwand.library.math.graph;

import java.util.Set;


/**
 * 
 * The root of all trivial graphs.
 * 
 * @author Andreas Stefl
 * 
 * @param <V> the type of the vertices.
 * @param <E> the type of the edges.
 * 
 */
public interface TrivialGraph<V, E extends Edge<V>> extends Graph<V, E> {
	
	/**
	 * Returns a set of the containing edges.
	 * 
	 * @return a set of the containing edges.
	 */
	public Set<E> getEdges();
	
	/**
	 * Returns all connected edges of the given vertex.
	 * 
	 * @param vertex the vertex.
	 * @return all connected edges of the given vertex.
	 */
	public Set<E> getConnectedEdges(V vertex);
	
}