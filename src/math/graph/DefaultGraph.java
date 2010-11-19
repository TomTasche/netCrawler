package math.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DefaultGraph<V> extends AbstractUndirectedGraph<V, DefaultEdge<V>> implements Multigraph<V, DefaultEdge<V>> {
	
	private Set<V> vertices;
	private List<DefaultEdge<V>> edges;
	
	
	public DefaultGraph() {
		vertices = new HashSet<V>();
		edges = new ArrayList<DefaultEdge<V>>();
	}
	
	
	public int getEdgeCount(DefaultEdge<V> edge) {
		int result = 0;
		
		for (DefaultEdge<V> e : edges) {
			if (e.equals(edge)) result++;
		}
		
		return result;
	}
	
	public Set<V> getVertices() {
		return new HashSet<V>(vertices);
	}
	public List<DefaultEdge<V>> getEdges() {
		return new ArrayList<DefaultEdge<V>>(edges);
	}
	
	public List<DefaultEdge<V>> getConnectedEdges(V vertex) {
		return new ArrayList<DefaultEdge<V>>(super.getConnectedEdges(vertex));
	}
	
	
	public boolean addVertex(V vertex) {
		return vertices.add(vertex);
	}
	public boolean addEdge(DefaultEdge<V> edge) {
		if (!vertices.contains(edge.getVertexA())) return false;
		if (!vertices.contains(edge.getVertexB())) return false;
		
		return edges.add(edge);
	}
	public boolean addEdge(V vertexA, V vertexB) {
		DefaultEdge<V> edge = new DefaultEdge<V>(vertexA, vertexB);
		
		return addEdge(edge);
	}
	
	public boolean removeVertex(V vertex) {
		return vertices.remove(vertex);
	}
	public boolean removeEdge(DefaultEdge<V> edge) {
		return edges.remove(edge);
	}
	public boolean removeEdge(V vertexA, V vertexB) {
		DefaultEdge<V> edge = new DefaultEdge<V>(vertexA, vertexB);
		
		return removeEdge(edge);
	}
	public boolean removeAllEdges(DefaultEdge<V> edge) {
		List<DefaultEdge<V>> remove = new ArrayList<DefaultEdge<V>>();
		remove.add(edge);
		
		return edges.removeAll(remove);
	}
	public boolean removeAllEdges(V vertexA, V vertexB) {
		DefaultEdge<V> edge = new DefaultEdge<V>(vertexA, vertexB);
		
		return removeAllEdges(edge);
	}
	
}