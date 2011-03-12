package at.andiwand.library.math.graph;

import java.util.Set;


/**
 * 
 * The root of all edges. <br>
 * 
 * @author Andreas Stefl
 * 
 * @param <V> the type of the vertices.
 * 
 */
public interface Edge<V> {
	
	/**
	 * Returns a string that represents the edge.
	 * 
	 * @return a string that represents the edge.
	 */
	public String toString();
	
	/**
	 * Returns <code>true</code> if the given object is equal to the edge.
	 * Normally the equality is defined by the containing vertices.
	 * 
	 * @param obj the object to compare.
	 * @return <code>true</code> if the given object is equal to the edge.
	 */
	public boolean equals(Object obj);
	
	/**
	 * Returns the hash code of the edge.
	 * 
	 * @return the hash code of the edge.
	 */
	public int hashCode();
	
	
	/**
	 * Returns the count of the connected vertices.
	 * 
	 * @return the count of the connected vertices.
	 */
	public int getVertexCount();
	
	/**
	 * Returns a set of the connected vertices.
	 * 
	 * @return a set of the connected vertices.
	 */
	public Set<V> getConnectedVertices();
	
}