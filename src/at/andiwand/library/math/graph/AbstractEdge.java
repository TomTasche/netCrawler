package at.andiwand.library.math.graph;

import java.util.Set;


public abstract class AbstractEdge<V> implements Edge<V> {
	
	@Override
	public String toString() {
		Set<V> connectedVertices = getConnectedVertices();
		
		return connectedVertices.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof AbstractEdge<?>)) return false;
		AbstractEdge<?> edge = (AbstractEdge<?>) obj;
		
		Set<?> connectedVerticesA = getConnectedVertices();
		Set<?> connectedVerticesB = edge.getConnectedVertices();
		
		return connectedVerticesA.equals(connectedVerticesB);
	}
	
	@Override
	public int hashCode() {
		Set<?> connectedVertices = getConnectedVertices();
		
		return connectedVertices.hashCode();
	}
	
	
	
	
	@Override
	public int getVertexCount() {
		return getConnectedVertices().size();
	}
	
}