package at.andiwand.library.graphics.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;

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
	public static final int DEFAULT_MAGNETIC_DISTANCE = 10;
	
	
	
	
	
	
	private Graph<Object, Edge<Object>> graphModel;
	
	
	private Set<DrawableVertex> vertices;
	private Set<DrawableEdge> edges;
	
	private Map<Class<?>, DrawableVertexFactory> vertexFactories;
	private Map<Class<?>, DrawableEdgeFactory> edgeFactories;
	
	private Map<Object, DrawableVertex> vertexMap;
	
	private GraphListener<Object, Edge<Object>> graphListener;
	
	
	private GraphLayout graphLayout;
	
	private boolean antialiasing;
	
	private boolean magneticLines;
	private int magneticDistance = DEFAULT_MAGNETIC_DISTANCE;
	private transient DrawableVertex magneticVertexX;
	private transient DrawableVertex magneticVertexY;
	
	
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
	public <V, E extends Edge<V>> JGraph(Graph<V, E> graph) {
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
			
			result.addAll(connectedVertices);
		}
		
		result.remove(vertex);
		
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
	public int getMagneticDistance() {
		return magneticDistance;
	}
	
	
	
	/**
	 * Forms the intern data structure with the given graph model. <br>
	 * It the graph is listenable, listeners will be installed to observe the
	 * graph model.
	 * 
	 * @param graph the graph model.
	 */
	@SuppressWarnings("unchecked")
	public synchronized <V, E extends Edge<V>> void setGraph(Graph<V, E> graph) {
		graphModel = (Graph<Object, Edge<Object>>) graph;
		
		vertices.clear();
		edges.clear();
		vertexMap.clear();
		
		if (graphModel instanceof ListenableGraph<?, ?>) {
			ListenableGraph<Object, Edge<Object>> listenableGraph =
				(ListenableGraph<Object, Edge<Object>>) graphModel;
			
			listenableGraph.removeListener(graphListener);
			listenableGraph = null;
			graphListener = null;
		}
		
		for (Object vertex : graphModel.getVertices()) {
			addVertex(vertex);
		}
		
		for (Edge<Object> edge : graphModel.getEdges()) {
			addEdge(edge);
		}
		
		if (graph instanceof ListenableGraph<?, ?>) {
			ListenableGraph<Object, Edge<Object>> listenableGraph =
				(ListenableGraph<Object, Edge<Object>>) graph;
			
			graphListener = new GraphUpdateAdapter();
			listenableGraph.addListener(graphListener);
		}
		
		graphLayout.reposition();
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
	public void setMagneticDistance(int magneticDistance) {
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
		graphLayout.reposition();
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
		graphLayout.reposition();
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
			
			if (magneticVertexX != null) {
				int x = magneticVertexX.getCenter().x;
				g.drawLine(x, 0, x, getHeight());
			}
			if (magneticVertexY != null) {
				int y = magneticVertexY.getCenter().y;
				g.drawLine(0, y, getWidth(), y);
			}
		}
		
		for (DrawableEdge edge : getEdges()) {
			edge.draw(g.create());
		}
		
		for (DrawableVertex vertex : getVertices()) {
			vertex.draw(g.create());
		}
	}
	
	
	
	
	private synchronized DrawableVertex vertexIntersection(Point point) {
		for (DrawableVertex vertex : vertices) {
			if (vertex.intersects(point)) return vertex;
		}
		
		return null;
	}
	
	
	private class DragAndDropHandler extends MouseAdapter {
		private DrawableVertex vertex;
		private Point centerOffset;
		private boolean moved;
		
		@Override
		public void mousePressed(MouseEvent e) {
			if (!isEnabled()) return;
			if (e.getButton() != MouseEvent.BUTTON1) return;
			
			Point point = e.getPoint();
			vertex = vertexIntersection(point);
			
			if (vertex == null) return;
			
			centerOffset = new Point(vertex.getCenter().x - point.x,
					vertex.getCenter().y - point.y);
			
			e.consume();
			moved = false;
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if (!isEnabled()) return;
			if (vertex == null) return;
			
			Point point = e.getPoint();
			
			if (point.getX() < 0) point.x = 0;
			if (point.getY() < 0) point.y = 0;
			if (point.getX() > getWidth()) point.x = getWidth();
			if (point.getY() > getHeight()) point.y = getHeight();
			
			Point newCenter = new Point(centerOffset.x + point.x,
					centerOffset.y + point.y);
			
			if (magneticLines) {
				magneticVertexX = magneticVertexY = null;
				
				int minX = magneticDistance;
				int minY = magneticDistance;
				for (DrawableVertex otherVertex : vertices) {
					if (otherVertex == vertex) continue;
					
					int distanceX = Math.abs(otherVertex.getCenter().x -
							newCenter.x);
					int distanceY = Math.abs(otherVertex.getCenter().y -
							newCenter.y);
					
					if (distanceX < minX) {
						magneticVertexX = otherVertex;
						minX = distanceX;
					}
					if (distanceY < minY) {
						magneticVertexY = otherVertex;
						minY = distanceY;
					}
				}
				
				if (magneticVertexX != null)
					newCenter.x = magneticVertexX.getCenter().x;
				if (magneticVertexY != null)
					newCenter.y = magneticVertexY.getCenter().y;
			}
			
			vertex.setCenter(newCenter);
			
			e.consume();
			moved = true;
			repaint();
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if (!isEnabled()) return;
			if (e.getButton() != MouseEvent.BUTTON1) return;
			
			vertex = null;
			
			magneticVertexX = magneticVertexY = null;
			
			if (moved) {
				e.consume();
			} else {
				graphMouseAdapter.mousePressed(e);
				graphMouseAdapter.mouseReleased(e);
			}
			repaint();
		}
	}
	
	
	//TODO: implement missing functions
	private class GraphMouseAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (e.isConsumed()) return;
			
			Point point = e.getPoint();
			DrawableVertex vertex = vertexIntersection(point);
			
			if (vertex == null) return;
			
			vertex.fireMouseEvent(e);
		}
		public void mouseReleased(MouseEvent e) {
			if (e.isConsumed()) return;
			
			Point point = e.getPoint();
			DrawableVertex vertex = vertexIntersection(point);
			
			if (vertex == null) return;
			
			vertex.fireMouseEvent(e);
		}
		public void mouseClicked(MouseEvent e) {
			if (e.isConsumed()) return;
			
			Point point = e.getPoint();
			DrawableVertex vertex = vertexIntersection(point);
			
			if (vertex == null) return;
			
			vertex.fireMouseEvent(e);
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