package at.andiwand.library.graphics.graph;

import java.awt.Dimension;

import at.andiwand.library.math.Vector2d;


public class CircleGraphLayout extends GraphLayout {
	
	public static final double DEFAULT_RADIUS_FACTOR = 40;
	public static final double DEFAULT_BORDER_SIZE = 100;
	
	
	private double radiusFactor;
	private double borderSize;
	
	private boolean fixedRadius;
	private double radius;
	
	
	public CircleGraphLayout(JGraph jGraph) {
		this(jGraph, DEFAULT_RADIUS_FACTOR, DEFAULT_BORDER_SIZE);
	}
	public CircleGraphLayout(JGraph jGraph, double radius) {
		this(jGraph, DEFAULT_RADIUS_FACTOR, DEFAULT_BORDER_SIZE);
		
		fixedRadius = true;
		this.radius = radius;
	}
	public CircleGraphLayout(JGraph jGraph, double radiusFactor, double borderSize) {
		super(jGraph);
		
		this.radiusFactor = radiusFactor;
		this.borderSize = borderSize;
	}
	
	
	
	public boolean isFixedRadius() {
		return fixedRadius;
	}
	
	public double getRadius() {
		return radius;
	}
	
	
	public void setFixedRadius(boolean fixedRadius) {
		this.fixedRadius = fixedRadius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	
	
	@Override
	public void reposition() {
		double radius;
		
		if (fixedRadius) radius = this.radius;
		else radius = jGraph.getVertexCount() * radiusFactor;
		
		double maxVertexSize = 0;
		for (DrawableVertex vertex : jGraph.getVertices()) {
			if (maxVertexSize < vertex.drawingRect().getWidth())
				maxVertexSize = vertex.drawingRect().getWidth();
			if (maxVertexSize < vertex.drawingRect().getHeight())
				maxVertexSize = vertex.drawingRect().getHeight();
		}
		
		double size = borderSize + maxVertexSize + 2 * radius;
		jGraph.setPreferredSize(new Dimension((int) size, (int) size));
		jGraph.revalidate();
		
		Vector2d middle = new Vector2d(size / 2);
		
		double angleStep = (2 * Math.PI) / jGraph.getVertexCount();
		double anglePosition = 0;
		
		for (DrawableVertex vertex : jGraph.getVertices()) {
			Vector2d position = new Vector2d(radius * Math.sin(anglePosition), -radius * Math.cos(anglePosition));
			
			vertex.setPosition(position.add(middle));
			
			anglePosition += angleStep;
		}
	}
	
	
	@Override
	public void positionUpdate() {
		reposition();
	}
	
}