package at.andiwand.library.math.graph;


public interface ListenableGraph<V, E extends Edge<V>> extends Graph<V, E> {
	
	public void addListener(GraphListener<V, E> listener);
	
	public void removeListener(GraphListener<V, E> listener);
	
}