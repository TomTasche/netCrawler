package at.andiwand.library.math.graph;

import java.util.HashSet;
import java.util.Set;


/**
 * 
 * An abstract implementation of the <code>Edge</code> interface. This class
 * implements simple methods to make it easier for extended classes to implement
 * an <code>Edge</code>.
 * 
 * @author Andreas Stefl
 * 
 * @param <V> the type of the vertices.
 */
public abstract class AbstractUndirectedEdge<V> extends AbstractEdge<V> implements UndirectedEdge<V> {
	
	/**
	 * Returns <code>true</code> if this edge is a loop.
	 * 
	 * @return <code>true</code> if this edge is a loop.
	 */
	@Override
	public boolean isLoop() {
		return getVertexA().equals(getVertexB());
	}
	
	
	/**
	 * Returns the count of the connected vertices. <br>
	 * If the edge is a loop, <code>1</code> is returned, otherwise
	 * <code>2</code> is returned.
	 * 
	 * @return the count of the connected vertices.
	 */
	@Override
	public int getVertexCount() {
		return isLoop() ? LOOP_VERTEX_COUNT : VERTEX_COUNT;
	}
	
	/**
	 * Returns a set of the connected vertices. <br>
	 * Creates a new mutable set, that contains the containing vertex/vertices
	 * of the edge. This method is implemented by <code>getVertexA()</code> and
	 * <code>getVertexB()</code>.
	 * 
	 * @return a set of the connected vertices.
	 */
	@Override
	public Set<V> getConnectedVertices() {
		Set<V> result = new HashSet<V>(VERTEX_COUNT);
		
		result.add(getVertexA());
		result.add(getVertexB());
		
		return result;
	}
	
}