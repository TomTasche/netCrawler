package at.rennweg.htl.netcrawler.graphics.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;

import math.Matrix2d;
import math.Rectangle;
import math.Vector2d;
import math.graph.Edge;

import at.rennweg.htl.netcrawler.math.graph.NetworkDevice;
import at.rennweg.htl.netcrawler.math.graph.SerialCable;
import graphics.graph.DrawableSingeEdge;
import graphics.graph.DrawableVertex;


public class DrawableSerialCable extends DrawableSingeEdge {
	
	public static final Vector2d DEFUALT_FLASH_SIZE = new Vector2d(5, 10);
	
	public static final Color DEFAULT_COLOR = Color.RED;
	
	
	private SerialCable<NetworkDevice> coveredEdge;
	
	private Vector2d flashSize = DEFUALT_FLASH_SIZE;
	private Color color = DEFAULT_COLOR;
	
	
	@SuppressWarnings("unchecked")
	public DrawableSerialCable(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		super(coveredEdge, connectedVertices);
		
		this.coveredEdge = (SerialCable<NetworkDevice>) (Edge<?>) coveredEdge;
	}
	
	
	@Override
	public Rectangle drawingRect() {
		return null;
	}
	
	public SerialCable<NetworkDevice> getSerialCable() {
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
		
		int x1 = (int) a.getX();
		int y1 = (int) a.getY();
		int x2 = (int) middle.sub(flashSize.div(2)).getX();
		int y2 = (int) middle.sub(flashSize.div(2)).getY();
		g.drawLine(x1, y1, x2, y2);
		
		x1 = (int) middle.add(flashSize.div(2)).getX();
		y1 = (int) middle.add(flashSize.div(2)).getY();
		g.drawLine(x1, y1, x2, y2);
		
		x2 = (int) b.getX();
		y2 = (int) b.getY();
		g.drawLine(x1, y1, x2, y2);
	}
	
}