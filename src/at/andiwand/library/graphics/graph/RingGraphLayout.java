package at.andiwand.library.graphics.graph;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.andiwand.library.math.Vector2d;


public class RingGraphLayout extends GraphLayout {
	
	public static final double DEFAULT_BORDER_SIZE = 100;
	
	public static double DEFAULT_RING_DISTANCE = 20; 
	
	
	private Set<DrawableVertex> positionedVertices;
	
	private Object rootModel;
	private DrawableVertex root;
	private List<List<DrawableVertex>> verticesRings;
	
	private double borderSize = DEFAULT_BORDER_SIZE;
	private double ringDistance = DEFAULT_RING_DISTANCE;
	
	
	public RingGraphLayout(JGraph jGraph) {
		this(jGraph, null);
	}
	public RingGraphLayout(JGraph jGraph, Object rootModel) {
		this(jGraph, null);
		
		this.rootModel = rootModel;
	}
	public RingGraphLayout(JGraph jGraph, DrawableVertex root) {
		super(jGraph);
		
		positionedVertices = new HashSet<DrawableVertex>();
		
		this.root = root;
		verticesRings = new ArrayList<List<DrawableVertex>>();
	}
	
	
	public void setRoot(DrawableVertex vertex) {
		root = vertex;
	}
	
	
	@Override
	public void reposition() {
		positionedVertices.clear();
		verticesRings.clear();
		
		Set<DrawableVertex> vertices = jGraph.getVertices();
		
		if (vertices.isEmpty()) return;
		
		if ((rootModel != null) && (root == null)) root = jGraph.getDrawableVertex(rootModel);
		if ((root == null) || (!vertices.contains(root))) root = chooseRandom(vertices);
		positionedVertices.add(root);
		List<DrawableVertex> rootRing = new ArrayList<DrawableVertex>();
		rootRing.add(root);
		verticesRings.add(rootRing);
		
		List<DrawableVertex> lastRing = rootRing;
		while (true) {
			List<DrawableVertex> newRing = new ArrayList<DrawableVertex>();
			
			for (DrawableVertex parentVertex : lastRing) {
				Set<DrawableVertex> connectedVertices = jGraph.getConnectedVertices(parentVertex);
				
				for (DrawableVertex vertex : connectedVertices) {
					if (positionedVertices.contains(vertex)) continue;
					
					newRing.add(vertex);
					positionedVertices.add(vertex);
				}
			}
			
			if (newRing.isEmpty()) break;
			
			verticesRings.add(newRing);
			lastRing = newRing;
		}
		
		root.setPosition(new Vector2d());
		
		double lastMaxSize = root.drawingRect().getSize().length();
		double lastRadius = 0;
		double left = 0;
		double right = 0;
		double bottom = 0;
		double top = 0;
		for (int i = 1; i < verticesRings.size(); i++) {
			List<DrawableVertex> ring = verticesRings.get(i);
			
			double maxSize = maxSize(ring);
			double radius = lastRadius + (lastMaxSize + maxSize) / 2 + ringDistance;
			
			double angleStep = (2 * Math.PI) / ring.size();
			double anglePosition = 0;
			for (DrawableVertex vertex : ring) {
				Vector2d position = new Vector2d(radius * Math.sin(anglePosition), -radius * Math.cos(anglePosition));
				vertex.setPosition(position);
				
				if (position.getX() < left) left = position.getX();
				if (position.getX() > right) right = position.getX();
				if (position.getY() < bottom) bottom = position.getY();
				if (position.getY() > top) top = position.getY();
				
				anglePosition += angleStep;
			}
			
			lastMaxSize = maxSize;
			lastRadius = radius;
		}
		
		Vector2d size = new Vector2d(right - left, top - bottom).abs().add(borderSize * 2);
		Vector2d middle = size.div(2);
		
		jGraph.setPreferredSize(new Dimension((int) size.getX(), (int) size.getY()));
		
		for (DrawableVertex vertex : positionedVertices) {
			vertex.setPosition(vertex.getPosition().add(middle));
		}
	}
	
	@Override
	public void positionUpdate() {
		reposition();
	}
	
	
	private static <E> E chooseRandom(Collection<E> collection) {
		ArrayList<E> list = new ArrayList<E>(collection);
		int randomItem = (int) (Math.random() * list.size());
		
		return list.get(randomItem);
	}
	
	private static <E> double maxSize(Collection<DrawableVertex> collection) {
		double result = 0;
		
		for (DrawableVertex vertex : collection) {
			double size = vertex.drawingRect().getSize().length();
			
			if (size > result) result = size;
		}
		
		return result;
	}
	
}