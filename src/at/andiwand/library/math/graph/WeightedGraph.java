package at.andiwand.library.math.graph;


public interface WeightedGraph<V, E extends WeightedEdge<V, W>, W> extends Graph<V, E> {
	
	public W getWeight();
	
}