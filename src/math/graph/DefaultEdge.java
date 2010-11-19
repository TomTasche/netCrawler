package math.graph;


public class DefaultEdge<V> extends AbstractUndirectedEdge<V> {
	
	private final V vertexA;
	private final V vertexB;
	
	
	public DefaultEdge(V vertexA, V vertexB) {
		this.vertexA = vertexA;
		this.vertexB = vertexB;
	}
	
	
	public String toString() {
		return "{" + vertexA.toString() + ", " + vertexB.toString() + "}";
	}
	
	
	public V getVertexA() {
		return vertexA;
	}
	public V getVertexB() {
		return vertexB;
	}
	
}