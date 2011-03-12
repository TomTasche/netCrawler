package at.andiwand.library.math.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 
 * An abstract implementation of the <code>UndirectedGraph</code> interface.
 * This class implements simple methods to make it easier for extended classes
 * to implement an <code>UndirectedGraph</code>.
 * 
 * @author Andreas Stefl
 * 
 * @param <V> the type of the vertices.
 * @param <E> the type of the edges.
 * 
 */

//TODO: isSimple()?
public abstract class AbstractUndirectedGraph<V, E extends AbstractUndirectedEdge<V>> extends AbstractGraph<V, E> implements UndirectedGraph<V, E> {
	
	/**
	 * Returns <code>true</code> if the graph is simple. <br>
	 * This method returns <code>false</code> if the graph contains a loop or
	 * multiple edges. <br>
	 * The speed of this method could be highly increased with a state variable
	 * that is updated on adding an edge.
	 * 
	 * @return <code>true</code> if the graph is simple.
	 */
	@Override
	public boolean isSimple() {
		Collection<E> edges = getEdges();
		
		for (E edge : edges) {
			if (edge.isLoop()) return false;
		}
		
		Set<E> edgeSet = new HashSet<E>(edges);
		
		return edges.size() == edgeSet.size();
	}
	
	/**
	 * Returns the degree of the given vertex. <br>
	 * This method calls <code>getConnectedEdges(V)</code> and counts all
	 * edges. Except for self-loops, these are counted twice. <br>
	 * The speed of this method could be highly increased with an map that is
	 * updated on adding or removing vertices and edges.
	 * 
	 * @param vertex the vertex.
	 * @return the degree of the given vertex.
	 */
	@Override
	public int getVertexDegree(V vertex) {
		List<E> edges = getConnectedEdges(vertex);
		int result = 0;
		
		for (E edge : edges) {
			if (edge.isLoop()) result += 2;
			else result++;
		}
		
		return result;
	}
	
}