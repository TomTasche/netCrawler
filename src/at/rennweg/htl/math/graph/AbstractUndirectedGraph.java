package at.rennweg.htl.math.graph;

import java.util.Collection;
import java.util.Set;


public abstract class AbstractUndirectedGraph<V, E extends AbstractUndirectedEdge<V>> extends AbstractGraph<V, E> implements UndirectedGraph<V, E> {
	
	@Override
	public Set<V> getNeighbours(V vertex) {
		return getConnectedVertices(vertex);
	}
	
	
	@Override
	public int getVertexDgree(V vertex) {
		Collection<E> edges = getEdges();
		int result = 0;
		
		for (E edge : edges) {
			Collection<V> connectedVerices = edge.getConnectedVertices();
			
			if (connectedVerices.contains(vertex)) result++;
		}
		
		return result;
	}
	
}