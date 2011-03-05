package at.andiwand.library.math.graph;

import java.util.Set;


public interface TrivialGraph<V, E extends Edge<V>> extends Graph<V, E> {
	
	public Set<E> getEdges();
	
	public Set<E> getConnectedEdges(V vertex);
	
}