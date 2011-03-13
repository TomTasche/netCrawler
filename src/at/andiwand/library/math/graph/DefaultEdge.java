package at.andiwand.library.math.graph;


/**
 * 
 * A simple final implementation of an undirected edge. <br>
 * It stores the connected vertices and implements the last two methods
 * <code>getVertexA()</code> and <code>getVertexB()</code>.
 * 
 * @author Andreas Stefl
 * 
 * @param <V> the type of the vertices.
 * 
 */
public class DefaultEdge<V> extends AbstractUndirectedEdge<V> {
	
	private final V vertexA;
	private final V vertexB;
	
	
	/**
	 * Creates a new self-loop with the given vertex.
	 * 
	 * @param vertex the vertex.
	 */
	public DefaultEdge(V vertex) {
		vertexA = vertexB = vertex;
	}
	
	/**
	 * Creates a new edge with the given vertices.
	 * 
	 * @param vertexA one of the both vertices.
	 * @param vertexB one of the both vertices.
	 */
	public DefaultEdge(V vertexA, V vertexB) {
		this.vertexA = vertexA;
		this.vertexB = vertexB;
	}
	
	
	public V getVertexA() {
		return vertexA;
	}
	public V getVertexB() {
		return vertexB;
	}
	
}