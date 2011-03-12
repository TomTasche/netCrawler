package at.andiwand.library.math.graph;


/**
 * 
 * The root of all undirected edges. <br>
 * 
 * @author Andreas Stefl
 * 
 * @param <V> the type of the vertices.
 * 
 */
public interface UndirectedEdge<V> extends Edge<V> {
	
	/**
	 * The vertex count of an undirected edge. Except for loops, these have just
	 * one vertex.
	 */
	public static final int VERTEX_COUNT = 2;
	
	/**
	 * The vertex count of an undirected loop.
	 */
	public static final int LOOP_VERTEX_COUNT = 1;
	
	
	
	/**
	 * Returns <code>true</code> if this edge is a loop.
	 * 
	 * @return <code>true</code> if this edge is a loop.
	 */
	public boolean isLoop();
	
	
	/**
	 * Returns one of the both connected vertices. The letter <code>A</code>
	 * says nothing about sequence of the vertices. There is no sequence.
	 * 
	 * @return one of the both connected vertices.
	 */
	public V getVertexA();
	
	/**
	 * Returns one of the both connected vertices. The letter <code>B</code>
	 * says nothing about sequence of the vertices. There is no sequence.
	 * 
	 * @return one of the both connected vertices.
	 */
	public V getVertexB();
	
}