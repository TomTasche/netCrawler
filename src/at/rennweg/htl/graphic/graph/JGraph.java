package at.rennweg.htl.graphic.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;

import math.graph.Edge;
import math.graph.Graph;


public class JGraph extends JComponent implements Graph<JVertex, JEdge> {
	
	private static final long serialVersionUID = 7065745631913860101L;
	
	
	
	
	private Graph<?, ? extends Edge<?>> graph;
	
	
	private JVertexFactory<?, ?> defaultVertexFactory;
	private JEdgeFactory<?, ?> defaultEdgeFactory;
	
	private Map<Class<?>, JVertexFactory<?, ?>> vertexFactories;
	private Map<Class<?>, JEdgeFactory<?, ?>> edgeFactories;
	
	private Set<JVertex> vertices;
	private List<JEdge> edges;
	
	
	
	
	public JGraph() {
		defaultVertexFactory = null;
		defaultEdgeFactory = null;
		
		vertexFactories = new HashMap<Class<?>, JVertexFactory<?, ?>>();
		edgeFactories = new HashMap<Class<?>, JEdgeFactory<?, ?>>();
		
		vertices = new HashSet<JVertex>();
		edges = new ArrayList<JEdge>();
	}
	
	
	
	
	public void setGraph(Graph<?, ? extends Edge<?>> graph) {
		this.graph = graph;
		
		// implement
	}
	
	
	public void setDefaultVertexFactory(JVertexFactory<?, ?> vertexFactory) {
		defaultVertexFactory = vertexFactory;
	}
	
	public void setDefaultEdgeFactory(JEdgeFactory<?, ?> edgeFactory) {
		defaultEdgeFactory = edgeFactory;
	}
	
	
	
	@Override
	public boolean isConnected() {
		return graph.isConnected();
	}
	
	@Override
	public boolean isSimple() {
		return graph.isSimple();
	}
	
	
	public Graph<?, ? extends Edge<?>> getGraph() {
		return graph;
	}
	
	
	@Override
	public int getVertexCount() {
		return graph.getVertexCount();
	}
	
	@Override
	public int getEdgeCount() {
		return graph.getEdgeCount();
	}
	
	
	@Override
	public Set<V> getVertices() {
		return new HashSet<V>(vertices);
	}
	
	@Override
	public Collection<E> getEdges() {
		return new ArrayList<E>(edges);
	}
	
	
	@Override
	public Set<V> getConnectedVertices(V vertex) {
		return null;
	}
	
	@Override
	public Collection<E> getConnectedEdges(V vertex) {
		return null;
	}
	
	
	@Override
	public Set<V> getNeighbours(V vertex) {
		return null;
	}
	
	
	
	@Override
	public boolean addVertex(V vertex) {
		return false;
	}
	
	@Override
	public boolean addEdge(E edge) {
		return false;
	}
	
	
	@Override
	public boolean removeVertex(V vertex) {
		return false;
	}
	
	@Override
	public boolean removeEdge(E edge) {
		return false;
	}
	
	@Override
	public boolean removeAllEdges(E edge) {
		return false;
	}
	
}