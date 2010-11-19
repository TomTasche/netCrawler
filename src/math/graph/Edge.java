package math.graph;

import java.util.Set;


public interface Edge<V> {
	
	public boolean equals(Object object);
	public int hashCode();
	
	
	
	public int getConnectionCount();
	
	public Set<V> getConnectedVertices();
	
}