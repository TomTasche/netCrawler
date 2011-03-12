package at.andiwand.library.math.graph;


/**
 * 
 * An instance of this interface can be added to a <code>ListenableGraph</code>
 * object.
 * 
 * @author Andreas Stefl
 * 
 * @param <V> the type of the vertices.
 * @param <E> the type of the edges.
 * 
 */
public interface GraphListener<V, E extends Edge<V>> {
	
	/**
	 * Is called when a new vertex was added on the listened graph.
	 * 
	 * @param vertex the new vertex.
	 */
	public void vertexAdded(V vertex);
	
	/**
	 * Is called when a new edge was added on the listened graph.
	 * 
	 * @param edge the new edge.
	 */
	public void edgeAdded(E edge);
	
	
	/**
	 * Is called when a vertex was removed from the listened graph.
	 * 
	 * @param vertex the removed vertex.
	 */
	public void vertexRemoved(V vertex);
	
	/**
	 * Is called when a edge was removed from the listened graph.
	 * 
	 * @param edge the removed edge.
	 */
	public void edgeRemoved(E edge);
	
}