package at.andiwand.library.graphics.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;

import at.andiwand.library.math.Vector2b;
import at.andiwand.library.math.Vector2d;
import at.andiwand.library.math.graph.Edge;
import at.andiwand.library.math.graph.Graph;
import at.andiwand.library.math.graph.GraphAdapter;
import at.andiwand.library.math.graph.GraphListener;
import at.andiwand.library.math.graph.ListenableGraph;


/**
 * 
 * A graphical representation of an <code>Graph</code> object. <br>
 * Uses <code>DrawableVertexFactory</code> and <code>DrawableEdgeFactory</code>
 * instances to build the intern data structure.
 * 
 * @author Andreas Stefl
 * 
 */

//TODO: thread safe
public class JGraph extends JComponent {
	
	private static final long serialVersionUID = -3715174655187422717L;
	
	/**
	 * The default distance of the magnetic lines.
	 */
	public static final double DEFAULT_MAGNETIC_DISTANCE = 10;
	
	
	
	
	
	
	private Set<DrawableVertex> vertices;
	private Set<DrawableEdge> edges;
	
	private Map<Class<?>, DrawableVertexFactory> vertexFactories;
	private Map<Class<?>, DrawableEdgeFactory> edgeFactories;
	
	private Map<Object, DrawableVertex> vertexMap;
	
	private ListenableGraph<Object, Edge<Object>> listenableGraph;
	private GraphListener<Object, Edge<Object>> graphListener;
	
	
	private GraphLayout graphLayout;
	
	private boolean antialiasing;
	
	private boolean magneticLines;
	private double magneticDistance = DEFAULT_MAGNETIC_DISTANCE;
	private transient Vector2b magneticFix = new Vector2b();
	private transient Vector2d magneticLine = new Vector2d();
	
	
	private DragAndDropHandler dragAndDropHandler;
	private GraphMouseAdapter graphMouseAdapter;
	
	
	
	
	
	/**
	 * Creates a empty <code>JGraph</code> instance. <br>
	 * The viewed graph is with the method <code>setGraph(Graph)</code> set.
	 */
	public JGraph() {
		vertices = new HashSet<DrawableVertex>();
		edges = new HashSet<DrawableEdge>();
		
		vertexFactories = new HashMap<Class<?>, DrawableVertexFactory>();
		vertexFactories.put(Object.class, new DefaultDrawableVertexFactory());
		edgeFactories = new HashMap<Class<?>, DrawableEdgeFactory>();
		edgeFactories.put(Object.class, new DefaultDrawableEdgeFactory());
		
		vertexMap = new HashMap<Object, DrawableVertex>();
		
		graphLayout = new CircleGraphLayout(this);
		
		dragAndDropHandler = new DragAndDropHandler();
		addMouseListener(dragAndDropHandler);
		addMouseMotionListener(dragAndDropHandler);
		
		graphMouseAdapter = new GraphMouseAdapter();
		addMouseListener(graphMouseAdapter);
		addMouseMotionListener(graphMouseAdapter);
		addMouseWheelListener(graphMouseAdapter);
	}
	
	/**
	 * Creates a <code>JGraph</code> instance with the given <code>Graph</code>
	 * instance. <br>
	 * If a <code>ListenableGraph</code> object is given, listeners will be
	 * installed.
	 * 
	 * @param graph the <code>Graph</code>instance.
	 */
	public JGraph(Graph<?, ? extends Edge<?>> graph) {
		this();
		
		setGraph(graph);
	}
	
	/**
	 * Creates a <code>JGraph</code> instance with the given
	 * <code>ListenableGraph</code> instance. <br>
	 * Listeners will be installed to observe the graph model.
	 * 
	 * @param graph the <code>ListenableGraph</code>instance.
	 */
	public JGraph(ListenableGraph<?, ? extends Edge<?>> graph) {
		this();
		
		setGraph(graph);
	}
	
	
	
	
	
	/**
	 * Returns the count of the containing vertices.
	 * 
	 * @return the count of the containing vertices.
	 */
	public int getVertexCount() {
		return vertices.size();
	}
	
	/**
	 * Returns the count of the containing edges.
	 * 
	 * @return the count of the containing edges.
	 */
	public int getEdgeCount() {
		return edges.size();
	}
	
	
	/**
	 * Returns an unmodifiable set of the containing vertices.
	 * 
	 * @return an unmodifiable set of the containing vertices.
	 */
	public Set<DrawableVertex> getVertices() {
		return Collections.unmodifiableSet(vertices);
	}
	
	/**
	 * Returns an unmodifiable set of the containing edges.
	 * 
	 * @return an unmodifiable set of the containing edges.
	 */
	public Collection<DrawableEdge> getEdges() {
		return Collections.unmodifiableSet(edges);
	}
	
	
	/**
	 * Returns the associated <code>DrawableVertex</code> object of the given
	 * vertex object.
	 * 
	 * @param vertex the vertex object.
	 * @return the associated <code>DrawableVertex</code> object.
	 */
	public DrawableVertex getDrawableVertex(Object vertex) {
		return vertexMap.get(vertex);
	}
	
	
	/**
	 * Returns the used <code>GraphLayout</code> instance.
	 * 
	 * @return the used <code>GraphLayout</code> instance.
	 */
	public GraphLayout getGraphLayout() {
		return graphLayout;
	}
	
	
	/**
	 * Returns the connected vertices of the given vertex.
	 * 
	 * @param vertex the <code>DrawableVertex</code> object.
	 * @return the connected vertices of the given vertex.
	 */
	public Set<DrawableVertex> getConnectedVertices(DrawableVertex vertex) {
		Set<DrawableVertex> result = new HashSet<DrawableVertex>();
		
		for (DrawableEdge edge : edges) {
			Set<DrawableVertex> connectedVertices = edge.getConnectedVertices();
			if (!connectedVertices.contains(vertex)) continue;
			
			connectedVertices.remove(vertex);
			result.addAll(connectedVertices);
		}
		
		return result;
	}
	
	
	/**
	 * Returns <code>true</code> if antialiasing is turned on.
	 * 
	 * @return <code>true</code> if antialiasing is turned on.
	 */
	public boolean isAntialiasing() {
		return antialiasing;
	}
	
	
	/**
	 * Returns <code>true</code> if magnetic raster is turned on.
	 * 
	 * @return <code>true</code> if magnetic raster is turned on.
	 */
	public boolean isMagneticLines() {
		return magneticLines;
	}
	
	/**
	 * Returns the distance of the magnetic lines.
	 * 
	 * @return the distance of the magnetic lines.
	 */
	public double getMagneticDistance() {
		return magneticDistance;
	}
	
	
	
	/**
	 * Forms the intern data structure with the given graph model. <br>
	 * 
	 * @param graph the graph model.
	 */
	@SuppressWarnings("unchecked")
	public synchronized void setGraph(Graph<?, ? extends Edge<?>> graph) {
		Graph<Object, Edge<Object>> objectGraph =
			(Graph<Object, Edge<Object>>) graph;
		
		vertices.clear();
		edges.clear();
		vertexMap.clear();
		
		if (listenableGraph != null) {
			listenableGraph.removeListener(graphListener);
			listenableGraph = null;
			graphListener = null;
		}
		
		for (Object vertex : objectGraph.getVertices()) {
			addVertex(vertex);
		}
		
		for (Edge<Object> edge : objectGraph.getEdges()) {
			addEdge(edge);
		}
		
		graphLayout.reposition();
	}
	
	/**
	 * Forms the intern data structure with the given graph model. <br>
	 * Listeners will be installed to observe the graph model.
	 * 
	 * @param graph the listenable graph model.
	 */
	@SuppressWarnings("unchecked")
	public void setGraph(ListenableGraph<?, ? extends Edge<?>> graph) {
		ListenableGraph<Object, Edge<Object>> listenableGraph = (ListenableGraph<Object, Edge<Object>>) graph;
		
		setGraph((Graph<?, ? extends Edge<?>>) listenableGraph);
		
		this.listenableGraph = listenableGraph;
		graphListener = new GraphUpdateAdapter();
		listenableGraph.addListener(graphListener);
	}
	
	
	/**
	 * Sets the <code>GraphLayout</code> instance. This layout controls the
	 * positions of the vertices.
	 * 
	 * @param graphLayout the <code>GraphLayout</code> instance.
	 */
	public void setGraphLayout(GraphLayout graphLayout) {
		this.graphLayout = graphLayout;
		
		graphLayout.reposition();
	}
	
	
	/**
	 * Sets the state of the antialiasing.
	 * 
	 * @param antialiasing <code>true</code> if the panel have to be drawn with
	 * antialiasing.
	 */
	public void setAntialiasing(boolean antialiasing) {
		this.antialiasing = antialiasing;
		
		repaint();
	}
	
	
	/**
	 * Sets the state of the magnetic lines.
	 * 
	 * @param magneticLines <code>true</code> if the panel have to use magnetic
	 * lines.
	 */
	public void setMagneticLines(boolean magneticLines) {
		this.magneticLines = magneticLines;
	}
	
	/**
	 * Sets the magnetic distance.
	 * 
	 * @param magneticDistance the magnetic distance.
	 */
	public void setMagneticDistance(double magneticDistance) {
		this.magneticDistance = magneticDistance;
	}
	
	
	
	
	private synchronized void addVertex(Object vertex) {
		Class<?> vertexClass = vertex.getClass();
		Class<?> matchingClass = Object.class;
		
		for (Class<?> factoryVertexClass : vertexFactories.keySet()) {
			if (matchingClass.isAssignableFrom(factoryVertexClass)) {
				if (factoryVertexClass.isAssignableFrom(vertexClass)) {
					matchingClass = factoryVertexClass;
				}
			}
		}
		
		DrawableVertexFactory vertexFactory = vertexFactories.get(matchingClass);
		
		DrawableVertex drawableVertex = vertexFactory.buildVertex(vertex);
		addVertex(drawableVertex);
	}
	private synchronized void addVertex(DrawableVertex vertex) {
		vertices.add(vertex);
		vertexMap.put(vertex.getCoveredVertex(), vertex);
		
		repaint();
		graphLayout.positionUpdate();
	}
	
	private synchronized void addEdge(Edge<Object> edge) {
		Class<?> edgeClass = edge.getClass();
		Class<?> matchingClass = Object.class;
		
		for (Class<?> factoryEdgeClass : edgeFactories.keySet()) {
			if (matchingClass.isAssignableFrom(factoryEdgeClass)) {
				if (factoryEdgeClass.isAssignableFrom(edgeClass)) {
					matchingClass = factoryEdgeClass;
				}
			}
		}
		
		DrawableEdgeFactory edgeFactory = edgeFactories.get(matchingClass);
		
		Set<DrawableVertex> connectedVertices = new HashSet<DrawableVertex>();
		for (Object vertex : edge.getConnectedVertices()) {
			connectedVertices.add(vertexMap.get(vertex));
		}
		
		DrawableEdge jEdge = edgeFactory.buildEdge(edge, connectedVertices);
		addEdge(jEdge);
	}
	private synchronized void addEdge(DrawableEdge edge) {
		edges.add(edge);
		
		repaint();
		graphLayout.positionUpdate();
	}
	
	
	/**
	 * Adds a <code>DrawableVertexFactory</code> instance which is bounded to
	 * responsible for the given class. <br>
	 * If there is a responsible factory for this class bounded, it will be
	 * removed.
	 * 
	 * @param clazz the responsibility of the factory.
	 * @param vertexFactory the factory instance.
	 */
	public synchronized void addVertexFactory(Class<?> clazz, DrawableVertexFactory vertexFactory) {
		vertexFactories.put(clazz, vertexFactory);
	}
	
	/**
	 * Adds a <code>DrawableEdgeFactory</code> instance which is bounded to
	 * responsible for the given class. <br>
	 * If there is a responsible factory for this class bounded, it will be
	 * removed.
	 * 
	 * @param clazz the responsibility of the factory.
	 * @param edgeFactory the factory instance.
	 */
	public synchronized void addEdgeFactory(Class<?> clazz, DrawableEdgeFactory edgeFactory) {
		edgeFactories.put(clazz, edgeFactory);
	}
	
	
	
	public synchronized void removeVertex(Object vertex) {
		//TODO: implementation
	}
	public synchronized void removeVertex(DrawableVertex vertex) {
		//TODO: implementation
	}
	
	public synchronized void removeEdge(Edge<Object> edge) {
		//TODO: implementation
	}
	public synchronized void removeEdge(DrawableEdge edge) {
		//TODO: implementation
	}
	
	
	/**
	 * Removes the bounded factory of the given class.
	 * 
	 * @param clazz the responsibility of the factory.
	 */
	public synchronized void removeVertexFactory(Class<?> clazz) {
		vertexFactories.remove(clazz);
	}
	
	/**
	 * Removes the bounded factory of the given class.
	 * 
	 * @param clazz the responsibility of the factory.
	 */
	public synchronized void removeEdgeFactory(Class<?> clazz) {
		edgeFactories.remove(clazz);
	}
	
	
	
	
	@Override
	protected synchronized void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics.create();
		
		if (antialiasing) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		if (magneticLines) {
			g.setColor(Color.LIGHT_GRAY);
			
			if (magneticFix.getX())
				g.drawLine((int) magneticLine.getX(), 0, (int) magneticLine.getX(), getHeight());
			if (magneticFix.getY())
				g.drawLine(0, (int) magneticLine.getY(), getWidth(), (int) magneticLine.getY());
		}
		
		for (DrawableEdge edge : getEdges()) {
			edge.draw(g);
		}
		
		for (DrawableVertex vertex : getVertices()) {
			vertex.draw(g);
		}
	}
	
	
	
	
	private synchronized DrawableVertex vertexIntersection(Vector2d point) {
		for (DrawableVertex vertex : vertices) {
			if (vertex.intersection(point)) return vertex;
		}
		
		return null;
	}
	
	private synchronized DrawableEdge edgeIntersection(Vector2d point) {
		for (DrawableEdge edge : edges) {
			if (edge.intersection(point)) return edge;
		}
		
		return null;
	}
	
	
	private class DragAndDropHandler extends MouseAdapter {
		private Vector2d startPoint;
		private DrawableVertex vertex;
		private Vector2d offset;
		private boolean moved;
		
		@Override
		public void mousePressed(MouseEvent e) {
			if (!isEnabled()) return;
			if (e.getButton() != MouseEvent.BUTTON1) return;
			
			startPoint = new Vector2d(e.getX(), e.getY());
			vertex = vertexIntersection(startPoint);
			
			if (vertex == null) return;
			
			offset = vertex.getCenterPosition().sub(startPoint);
			
			e.consume();
			moved = false;
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if (!isEnabled()) return;
			if (vertex == null) return;
			
			Vector2d point = new Vector2d(e.getX(), e.getY());
			
			if (point.getX() < 0) point = point.setX(0);
			if (point.getY() < 0) point = point.setY(0);
			if (point.getX() > getWidth()) point = point.setX(getWidth());
			if (point.getY() > getHeight()) point = point.setY(getHeight());
			
			Vector2d newPosition = point.add(offset);
			
			if (magneticLines) {
				magneticFix = new Vector2b();
				
				Vector2d magnaticTransformation = new Vector2d();
				Vector2d minDistance = new Vector2d(magneticDistance);
				for (DrawableVertex otherVertex : vertices) {
					if (otherVertex == vertex) continue;
					
					Vector2d distance = otherVertex.getCenterPosition().sub(newPosition);
					Vector2d absDistance = distance.abs();
					
					if (absDistance.getX() <= minDistance.getX()) {
						magnaticTransformation = magnaticTransformation.setX(distance.getX());
						minDistance = minDistance.setX(absDistance.getX());
						
						magneticFix = magneticFix.setX(true);
					}
					if (absDistance.getY() <= minDistance.getY()) {
						magnaticTransformation = magnaticTransformation.setY(distance.getY());
						minDistance = minDistance.setY(absDistance.getY());
						
						magneticFix = magneticFix.setY(true);
					}
				}
				
				newPosition = newPosition.add(magnaticTransformation);
				
				if (magneticFix.getX()) {
					magneticLine = magneticLine.setX(newPosition.getX());
				}
				if (magneticFix.getY()) {
					magneticLine = magneticLine.setY(newPosition.getY());
				}
			}
			
			vertex.setCenterPosition(newPosition);
			
			e.consume();
			moved = true;
			repaint();
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if (!isEnabled()) return;
			if (e.getButton() != MouseEvent.BUTTON1) return;
			
			vertex = null;
			
			magneticFix = new Vector2b();
			
			if (moved) {
				e.consume();
			} else {
				graphMouseAdapter.mousePressed(e);
				graphMouseAdapter.mouseReleased(e);
			}
			repaint();
		}
	}
	
	
	private class GraphMouseAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (e.isConsumed()) return;
			
			Vector2d point = new Vector2d(e.getX(), e.getY());
			
			DrawableVertex vertex = vertexIntersection(point);
			
			
			if ((vertex != null) && (vertex.getMouseAdapter() != null)) {
				vertex.getMouseAdapter().mousePressed(e);
			} else {
				DrawableEdge edge = edgeIntersection(point);
				
				if ((edge != null) && (edge.getMouseAdapter() != null))
					edge.getMouseAdapter().mousePressed(e);
			}
		}
		public void mouseReleased(MouseEvent e) {
			if (e.isConsumed()) return;
			
			Vector2d point = new Vector2d(e.getX(), e.getY());
			
			DrawableVertex vertex = vertexIntersection(point);
			
			if ((vertex != null) && (vertex.getMouseAdapter() != null)) {
				vertex.getMouseAdapter().mouseReleased(e);
			} else {
				DrawableEdge edge = edgeIntersection(point);
				
				if ((edge != null) && (edge.getMouseAdapter() != null))
					edge.getMouseAdapter().mouseReleased(e);
			}
		}
		public void mouseClicked(MouseEvent e) {
			if (e.isConsumed()) return;
			
			Vector2d point = new Vector2d(e.getX(), e.getY());
			
			DrawableVertex vertex = vertexIntersection(point);
			
			if ((vertex != null) && (vertex.getMouseAdapter() != null)) {
				vertex.getMouseAdapter().mouseClicked(e);
			} else {
				DrawableEdge edge = edgeIntersection(point);
				
				if ((edge != null) && (edge.getMouseAdapter() != null))
					edge.getMouseAdapter().mouseClicked(e);
			}
		}
		
		public void mouseMoved(MouseEvent e) {
			if (e.isConsumed()) return;
			
			Vector2d point = new Vector2d(e.getX(), e.getY());
			
			DrawableVertex vertex = vertexIntersection(point);
			
			if ((vertex != null) && (vertex.getMouseAdapter() != null)) {
				vertex.getMouseAdapter().mouseMoved(e);
			} else {
				DrawableEdge edge = edgeIntersection(point);
				
				if ((edge != null) && (edge.getMouseAdapter() != null))
					edge.getMouseAdapter().mouseMoved(e);
			}
		}
		public void mouseDragged(MouseEvent e) {
			if (e.isConsumed()) return;
			
			Vector2d point = new Vector2d(e.getX(), e.getY());
			
			DrawableVertex vertex = vertexIntersection(point);
			
			if ((vertex != null) && (vertex.getMouseAdapter() != null)) {
				vertex.getMouseAdapter().mouseDragged(e);
			} else {
				DrawableEdge edge = edgeIntersection(point);
				
				if ((edge != null) && (edge.getMouseAdapter() != null))
					edge.getMouseAdapter().mouseDragged(e);
			}
		}
		
		public void mouseWheelMoved(MouseWheelEvent e) {
			Vector2d point = new Vector2d(e.getX(), e.getY());
			
			DrawableVertex vertex = vertexIntersection(point);
			
			if ((vertex != null) && (vertex.getMouseAdapter() != null)) {
				vertex.getMouseAdapter().mouseWheelMoved(e);
			} else {
				DrawableEdge edge = edgeIntersection(point);
				
				if ((edge != null) && (edge.getMouseAdapter() != null))
					edge.getMouseAdapter().mouseWheelMoved(e);
			}
		}
	}
	
	
	private class GraphUpdateAdapter extends GraphAdapter<Object, Edge<Object>> {
		public void vertexAdded(Object vertex) {
			addVertex(vertex);
		}
		public void edgeAdded(Edge<Object> edge) {
			addEdge(edge);
		}
		
		public void vertexRemoved(Object vertex) {
			removeVertex(vertex);
		}
		public void edgeRemoved(Edge<Object> edge) {
			removeEdge(edge);
		}
	}
	
}