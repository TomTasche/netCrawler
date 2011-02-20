package at.rennweg.htl.netcrawler.graphics.graph;


import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;

import at.andiwand.library.graphics.GraphicsUtil;
import at.andiwand.library.graphics.graph.DrawableSingeEdge;
import at.andiwand.library.graphics.graph.DrawableVertex;
import at.andiwand.library.math.Matrix2d;
import at.andiwand.library.math.Rectangle;
import at.andiwand.library.math.Vector2d;
import at.andiwand.library.math.graph.Edge;
import at.rennweg.htl.netcrawler.network.graph.SerialCable;


public class DrawableSerialCable extends DrawableSingeEdge {
	
	public static final Vector2d DEFUALT_FLASH_SIZE = new Vector2d(5, 10);
	
	public static final Color DEFAULT_COLOR = Color.RED;
	
	
	private SerialCable coveredEdge;
	
	private Vector2d flashSize = DEFUALT_FLASH_SIZE;
	private Color color = DEFAULT_COLOR;
	
	
	public DrawableSerialCable(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		super(coveredEdge, connectedVertices);
		
		if (!(((Edge<?>) coveredEdge) instanceof SerialCable))
			throw new IllegalArgumentException("coveredEdge is no instance of SerialCable");
		
		this.coveredEdge = (SerialCable) (Edge<?>) coveredEdge;
	}
	
	
	@Override
	public Rectangle drawingRect() {
		return null;
	}
	
	public SerialCable getSerialCable() {
		return coveredEdge;
	}
	
	
	@Override
	public boolean intersection(Vector2d point) {
		return false;
	}
	
	
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		
		Vector2d a = vertexA.getPosition();
		Vector2d b = vertexB.getPosition();
		Vector2d middle = a.add(b).div(2);
		Vector2d distance = a.sub(b);
		double angle = Math.atan2(distance.getY(), distance.getX());
		Matrix2d rotation = Matrix2d.rotation(angle);
		Vector2d flashSize = rotation.mul(this.flashSize);
		
		Vector2d from = a;
		Vector2d to = middle.sub(flashSize.div(2));
		GraphicsUtil.drawLine(g, from, to);
		
		from = to;
		to = middle.add(flashSize.div(2));
		GraphicsUtil.drawLine(g, from, to);
		
		from = to;
		to = b;
		GraphicsUtil.drawLine(g, from, to);
	}
	
}