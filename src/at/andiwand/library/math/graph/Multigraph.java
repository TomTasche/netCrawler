package at.andiwand.library.math.graph;

import java.util.List;


public interface Multigraph<V, E extends Edge<V>> extends Graph<V, E> {
	
	public int getEdgeCount(E edge);
	
	public List<E> getEdges();
	
	public List<E> getConnectedEdges(V vertex);
	
}