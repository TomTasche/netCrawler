package at.andiwand.library.math.graph;


/**
 * 
 * The root of all undirected graphs. <br>
 * 
 * @author Andreas Stefl
 * 
 * @param <V> the type of the vertices.
 * @param <E> the type of the edges.
 * 
 */
public interface UndirectedGraph<V, E extends UndirectedEdge<V>> extends Graph<V, E> {
	
	/**
	 * Returns the degree of the given vertex.
	 * 
	 * @param vertex the vertex.
	 * @return the degree of the given vertex.
	 */
	public int getVertexDegree(V vertex);
	
}