package graphics.graph;

import java.awt.Graphics;

import math.Rectangle;
import math.Vector2d;


public class DefaultDrawableVertex extends DrawableVertex {
	
	private static final double DEFAULT_RADIUS = 8;
	
	
	
	private double radius;
	
	
	
	public DefaultDrawableVertex(Object coveredVertex) {
		this(coveredVertex, DEFAULT_RADIUS);
	}
	public DefaultDrawableVertex(Object coveredVertex, double radius) {
		super(coveredVertex);
		
		this.radius = radius;
	}
	
	
	
	@Override
	public Rectangle drawingRect() {
		return new Rectangle(getPosition(), new Vector2d(radius * 2));
	}
	
	
	@Override
	public boolean intersection(Vector2d point) {
		return getPosition().sub(point).length() <= radius;
	}
	
	
	
	@Override
	public void draw(Graphics g) {
		int x = (int) (getPosition().getX() - radius);
		int y = (int) (getPosition().getY() - radius);
		int size = (int) (radius * 2);
		
		g.fillOval(x, y, size, size);
	}
	
	
}