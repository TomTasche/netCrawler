package at.andiwand.library.math.graph;


/**
 * 
 * The root of all listenable graphs. <br>
 * 
 * @author Andreas Stefl
 * 
 * @param <V> the type of the vertices.
 * @param <E> the type of the edges.
 * 
 */
public interface ListenableGraph<V, E extends Edge<V>> extends Graph<V, E> {
	
	/**
	 * Adds a new listener to the graph.
	 * 
	 * @param listener the listener.
	 */
	public void addListener(GraphListener<V, E> listener);
	
	/**
	 * Removes a listener from the graph.
	 * 
	 * @param listener the listener.
	 */
	public void removeListener(GraphListener<V, E> listener);
	
}