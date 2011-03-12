package at.andiwand.library.math.graph;

import java.util.List;


/**
 * 
 * The root of all multigraphs.
 * 
 * @author Andreas Stefl
 * 
 * @param <V> the type of the vertices.
 * @param <E> the type of the edges.
 * 
 */
public interface Multigraph<V, E extends Edge<V>> extends Graph<V, E> {
	
	/**
	 * Returns the count of all equal edges in the graph.
	 * 
	 * @param edge
	 * @return the count of all equal edges in the graph.
	 */
	public int getEdgeCount(E edge);
	
	/**
	 * Returns a list of the containing edges. <br>
	 * The sequence of the list has no significance.
	 * 
	 * @return a list of the containing edges.
	 */
	public List<E> getEdges();
	
	/**
	 * Returns all connected edges of the given vertex. <br>
	 * The sequence of the list has no significance.
	 * 
	 * @param vertex the vertex.
	 * @return all connected edges of the given vertex.
	 */
	public List<E> getConnectedEdges(V vertex);
	
}