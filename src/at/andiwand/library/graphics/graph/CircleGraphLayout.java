package at.andiwand.library.graphics.graph;

import java.awt.Dimension;
import java.awt.Point;


public class CircleGraphLayout extends GraphLayout {
	
	public static final int DEFAULT_RADIUS_FACTOR = 40;
	public static final int DEFAULT_BORDER_SIZE = 100;
	
	
	private double radiusFactor;
	private int borderSize;
	
	private boolean fixedRadius;
	private int radius;
	
	
	public CircleGraphLayout(JGraph jGraph) {
		this(jGraph, DEFAULT_RADIUS_FACTOR, DEFAULT_BORDER_SIZE);
	}
	public CircleGraphLayout(JGraph jGraph, int radius) {
		this(jGraph, DEFAULT_RADIUS_FACTOR, DEFAULT_BORDER_SIZE);
		
		fixedRadius = true;
		this.radius = radius;
	}
	public CircleGraphLayout(JGraph jGraph, int radiusFactor, int borderSize) {
		super(jGraph);
		
		this.radiusFactor = radiusFactor;
		this.borderSize = borderSize;
	}
	
	
	
	public boolean isFixedRadius() {
		return fixedRadius;
	}
	
	public int getRadius() {
		return radius;
	}
	
	
	public void setFixedRadius(boolean fixedRadius) {
		this.fixedRadius = fixedRadius;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
		
		reposition();
	}
	
	
	
	@Override
	public void reposition() {
		int radius;
		
		if (fixedRadius) radius = this.radius;
		else radius = (int) (jGraph.getVertexCount() * radiusFactor);
		
		int maxVertexSize = 0;
		for (DrawableVertex vertex : jGraph.getVertices()) {
			if (maxVertexSize < vertex.getWidth())
				maxVertexSize = vertex.getWidth();
			if (maxVertexSize < vertex.getHeight())
				maxVertexSize = vertex.getHeight();
		}
		
		int size = borderSize + maxVertexSize + 2 * radius;
		jGraph.setPreferredSize(new Dimension((int) size, (int) size));
		jGraph.revalidate();
		
		Point middle = new Point(size / 2, size / 2);
		
		double angleStep = (2 * Math.PI) / jGraph.getVertexCount();
		double anglePosition = 0;
		
		for (DrawableVertex vertex : jGraph.getVertices()) {
			Point position = new Point((int) (radius * Math.sin(anglePosition)),
					(int) (-radius * Math.cos(anglePosition)));
			
			vertex.setCenter(middle.x + position.x, middle.y + position.y);
			
			anglePosition += angleStep;
		}
	}
	
}