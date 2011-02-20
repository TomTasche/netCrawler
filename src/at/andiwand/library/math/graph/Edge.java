package at.andiwand.library.math.graph;

import java.util.Set;


public interface Edge<V> {
	
	public String toString();
	public boolean equals(Object obj);
	public int hashCode();
	
	
	
	public int getVertexCount();
	
	public Set<V> getConnectedVertices();
	
}