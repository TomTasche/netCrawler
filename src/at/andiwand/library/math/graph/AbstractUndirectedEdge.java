package at.andiwand.library.math.graph;

import java.util.HashSet;
import java.util.Set;


public abstract class AbstractUndirectedEdge<V> extends AbstractEdge<V> implements UndirectedEdge<V> {
	
	@Override
	public boolean isLoop() {
		return getVertexA().equals(getVertexB());
	}
	
	
	@Override
	public int getVertexCount() {
		return VERTEX_COUNT;
	}
	
	@Override
	public Set<V> getConnectedVertices() {
		Set<V> result = new HashSet<V>(VERTEX_COUNT);
		
		result.add(getVertexA());
		result.add(getVertexB());
		
		return result;
	}
	
}