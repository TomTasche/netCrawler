package at.andiwand.library.math.graph;



/**
 * 
 * An instance of this class can be added to a <code>ListenableGraph</code>
 * object. It just implements the <code>GraphListener</code> interface and
 * provides empty methods. This class is abstract because an instance would be
 * just an empty listener.
 * 
 * @author Andreas Stefl
 * 
 * @param <V> the type of the vertices.
 * @param <E> the type of the edges.
 * 
 */
public abstract class GraphAdapter<V, E extends Edge<V>> implements GraphListener<V, E> {
	
	/**
	 * Is called when a new vertex was added on the listened graph. <br>
	 * This is just an empty method and should be overwritten for use.
	 * 
	 * @param vertex the new vertex.
	 */
	@Override
	public void vertexAdded(V vertex) {}
	
	/**
	 * Is called when a new edge was added on the listened graph. <br>
	 * This is just an empty method and should be overwritten for use.
	 * 
	 * @param edge the new edge.
	 */
	@Override
	public void edgeAdded(E edge) {}
	
	
	/**
	 * Is called when a vertex was removed from the listened graph. <br>
	 * This is just an empty method and should be overwritten for use.
	 * 
	 * @param vertex the removed vertex.
	 */
	@Override
	public void vertexRemoved(V vertex) {}
	
	/**
	 * Is called when a edge was removed from the listened graph. <br>
	 * This is just an empty method and should be overwritten for use.
	 * 
	 * @param edge the removed edge.
	 */
	@Override
	public void edgeRemoved(E edge) {}
	
}