package at.rennweg.htl.math.graph;


public interface WeightedEdge<V, W> extends Edge<V>, Comparable<WeightedEdge<V, W>> {
	
	public W getWeight();
	
}