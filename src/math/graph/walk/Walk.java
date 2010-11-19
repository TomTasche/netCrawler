package math.graph.walk;

import java.util.List;
import java.util.Set;

import math.graph.Edge;



public interface Walk<V, E extends Edge<V>> {
	
	public boolean isClosed();
	public boolean isTrail();
	public boolean isCircuit();
	public boolean isChain();
	public boolean isCycle();
	
	public List<V> getVertexSequence();
	public List<E> getEdgeSequence();
	
	public Set<V> getVertices();
	public Set<E> getEdges();
	
}