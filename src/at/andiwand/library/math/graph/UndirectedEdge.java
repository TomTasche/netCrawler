package at.andiwand.library.math.graph;


public interface UndirectedEdge<V> extends Edge<V> {
	
	public boolean isLoop();
	
	public V getVertexA();
	public V getVertexB();
	
}