package at.andiwand.library.math.graph;

import java.util.Collection;
import java.util.Set;


// TODO: isConnected()? isSimple()?
public interface Graph<V, E extends Edge<V>> {
	
	public String toString();
	public boolean equals(Object obj);
	public int hashCode();
	
	
	public boolean isConnected();
	public boolean isSimple();
	
	public int getVertexCount();
	public int getEdgeCount();
	
	public Set<V> getVertices();
	public Collection<E> getEdges();
	
	public Set<V> getConnectedVertices(V vertex);
	public Set<V> getConnectedVertices(E edge);
	public Collection<E> getConnectedEdges(V vertex);
	
	
	public boolean addVertex(V vertex);
	public boolean addEdge(E edge);
	
	public boolean removeVertex(V vertex);
	public boolean removeEdge(E edge);
	public boolean removeAllEdges(E edge);
	
}