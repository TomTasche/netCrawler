package math.graph;


public interface UndirectedGraph<V, E extends UndirectedEdge<V>> extends Graph<V, E> {
	
	public int getVertexDgree(V vertex);
	
}