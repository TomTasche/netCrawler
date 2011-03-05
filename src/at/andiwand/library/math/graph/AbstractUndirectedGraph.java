package at.andiwand.library.math.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class AbstractUndirectedGraph<V, E extends AbstractUndirectedEdge<V>> extends AbstractGraph<V, E> implements UndirectedGraph<V, E> {
	
	@Override
	public boolean isSimple() {
		Collection<E> edges = getEdges();
		
		for (E edge : edges) {
			if (edge.isLoop()) return false;
		}
		
		Set<E> edgeSet = new HashSet<E>(edges);
		
		return edges.size() == edgeSet.size();
	}
	
	@Override
	public int getVertexDgree(V vertex) {
		List<E> edges = getConnectedEdges(vertex);
		int result = 0;
		
		for (E edge : edges) {
			if (edge.isLoop()) result += 2;
			else result++;
		}
		
		return result;
	}
	
}