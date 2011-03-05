package at.andiwand.library.math.graph;


public interface UndirectedEdge<V> extends Edge<V> {
	
	public static final int VERTEX_COUNT = 2;
	
	
	public boolean isLoop();
	
	public V getVertexA();
	public V getVertexB();
	
}