package at.andiwand.library.graphics.graph;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class RingGraphLayout extends GraphLayout {
	
	public static final int DEFAULT_BORDER_SIZE = 100;
	
	public static int DEFAULT_RING_DISTANCE = 40; 
	
	
	
	private Set<DrawableVertex> positionedVertices;
	
	private Object rootModel;
	private DrawableVertex root;
	private List<List<DrawableVertex>> verticesRings;
	
	private int borderSize = DEFAULT_BORDER_SIZE;
	private int ringDistance = DEFAULT_RING_DISTANCE;
	
	
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
		
		root.setCenter(new Point());
		
		double lastMaxSize = root.getDiagonal();
		int lastRadius = 0;
		int left = 0;
		int right = 0;
		int bottom = 0;
		int top = 0;
		for (int i = 1; i < verticesRings.size(); i++) {
			List<DrawableVertex> ring = verticesRings.get(i);
			
			double maxSize = maxSize(ring);
			int radius = (int) (lastRadius + (lastMaxSize + maxSize(ring)) / 2
					+ ringDistance);
			
			double angleStep = (2 * Math.PI) / ring.size();
			double anglePosition = 0;
			for (DrawableVertex vertex : ring) {
				Point position = new Point(
						(int) (radius * Math.sin(anglePosition)),
						(int) (-radius * Math.cos(anglePosition)));
				if ((i & 1) != 0) {
					int tmp = position.x;
					position.x = position.y;
					position.y = tmp;
				}
				vertex.setCenter(position);
				
				if (position.getX() < left) left = position.x;
				if (position.getX() > right) right = position.x;
				if (position.getY() < bottom) bottom = position.y;
				if (position.getY() > top) top = position.y;
				
				anglePosition += angleStep;
			}
			
			lastMaxSize = maxSize;
			lastRadius = radius;
		}
		
		int width = Math.abs(right - left) + borderSize * 2;
		int height = Math.abs(top - bottom) + borderSize * 2;
		
		Dimension size = new Dimension(width, height);
		jGraph.setPreferredSize(size);
		jGraph.revalidate();
		
		Point middle = new Point(width / 2, height / 2);
		
		for (DrawableVertex vertex : positionedVertices) {
			Point position = vertex.getCenter();
			vertex.setCenter(middle.x + position.x, middle.y + position.y);
		}
	}
	
	
	private static <E> E chooseRandom(Collection<E> collection) {
		ArrayList<E> list = new ArrayList<E>(collection);
		int randomItem = (int) (Math.random() * list.size());
		
		return list.get(randomItem);
	}
	
	private static <E> double maxSize(Collection<DrawableVertex> collection) {
		double result = 0;
		
		for (DrawableVertex vertex : collection) {
			double size = vertex.getDiagonal();
			
			if (size > result) result = size;
		}
		
		return result;
	}
	
}