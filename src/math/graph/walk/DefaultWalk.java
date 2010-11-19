package math.graph.walk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import math.graph.DefaultEdge;



public class DefaultWalk<V> extends AbstractUndirectedWalk<V, DefaultEdge<V>> {
	
	private final List<V> vertexSequnence;
	private final List<DefaultEdge<V>> edgeSequnence;
	
	
	public DefaultWalk(List<V> vertexSequnence) {
		if (vertexSequnence.size() < 2) throw new IllegalArgumentException("list is too small!");
		
		this.vertexSequnence = new ArrayList<V>(vertexSequnence);
		edgeSequnence = new ArrayList<DefaultEdge<V>>();
		
		for (int i = 1; i < vertexSequnence.size(); i++) {
			V vertexA = this.vertexSequnence.get(i - 1);
			V vertexB = this.vertexSequnence.get(i);
			DefaultEdge<V> edge = new DefaultEdge<V>(vertexA, vertexB);
			
			edgeSequnence.add(edge);
		}
	}
	
	
	public List<V> getVertexSequence() {
		return new ArrayList<V>(vertexSequnence);
	}
	public List<DefaultEdge<V>> getEdgeSequence() {
		return new ArrayList<DefaultEdge<V>>(edgeSequnence);
	}
	
	public Set<V> getVertices() {
		return new HashSet<V>(vertexSequnence);
	}
	public Set<DefaultEdge<V>> getEdges() {
		return new HashSet<DefaultEdge<V>>(edgeSequnence);
	}
	
}