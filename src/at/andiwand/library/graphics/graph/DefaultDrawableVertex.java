package at.andiwand.library.graphics.graph;

import java.awt.Color;
import java.awt.Graphics;


public class DefaultDrawableVertex extends DrawableVertex {
	
	private static final long serialVersionUID = 7160267969933238534L;
	
	private static final int DEFAULT_RADIUS = 8;
	private static final Color DEFAULT_COLOR = Color.BLACK;
	
	
	
	
	private Color color = DEFAULT_COLOR;
	
	
	
	public DefaultDrawableVertex(Object coveredVertex) {
		this(coveredVertex, DEFAULT_RADIUS);
	}
	public DefaultDrawableVertex(Object coveredVertex, int radius) {
		super(coveredVertex);
		
		int size = radius * 2;
		setSize(size, size);
	}
	
	
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(getX(), getY(), getWidth(), getHeight());
	}
	
	
}