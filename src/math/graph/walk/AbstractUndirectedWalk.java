package math.graph.walk;

import java.util.List;
import java.util.Set;

import math.graph.AbstractUndirectedEdge;



public abstract class AbstractUndirectedWalk<V, E extends AbstractUndirectedEdge<V>> extends AbstractWalk<V, E> implements Walk<V, E> {
	
	public boolean isClosed() {
		List<V> vertexSequnence = getVertexSequence();
		
		V first = vertexSequnence.get(0);
		V last = vertexSequnence.get(vertexSequnence.size() - 1);
		
		return first.equals(last);
	}
	public boolean isTrail() {
		List<E> edgeSequnence = getEdgeSequence();
		Set<E> edges = getEdges();
		
		return edgeSequnence.size() == edges.size();
	}
	public boolean isCircuit() {
		return isClosed() && isTrail();
	}
	public boolean isChain() {
		List<V> vertexSequnence = getVertexSequence();
		Set<V> vertices = getVertices();
		
		if (isClosed()) return vertexSequnence.size() - 1 == vertices.size();
		else return vertexSequnence.size() == vertices.size();
	}
	public boolean isCycle() {
		return isClosed() && isChain();
	}
	
}