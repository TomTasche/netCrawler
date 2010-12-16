package at.rennweg.htl.netcrawler.graphics.graph;

import graphics.graph.DrawableEdge;
import graphics.graph.DrawableVertex;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import at.rennweg.htl.netcrawler.math.graph.EthernetCable;
import at.rennweg.htl.netcrawler.math.graph.NetworkDevice;

import math.Rectangle;
import math.Vector2d;
import math.graph.Edge;


public class DrawableEthernetCable extends DrawableEdge {
	
	public static final Color DEFAULT_COLOR = Color.BLACK;
	public static final double DEFAULT_SEPERATION_LENGTH = 10;
	
	
	private EthernetCable<NetworkDevice> coveredEdge;
	
	private Color color = DEFAULT_COLOR;
	private double seperationLength = DEFAULT_SEPERATION_LENGTH;
	
	
	@SuppressWarnings("unchecked")
	public DrawableEthernetCable(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		super(coveredEdge, connectedVertices);
		
		if (!(coveredEdge instanceof EthernetCable<?>))
			throw new IllegalArgumentException("coveredEdge is no instance of EthernetCable");
		
		this.coveredEdge = (EthernetCable<NetworkDevice>) (Edge<?>) coveredEdge;
	}
	
	
	@Override
	public Rectangle drawingRect() {
		return null;
	}
	
	@Override
	public boolean intersection(Vector2d point) {
		return false;
	}
	
	
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		
		int vertexCount = coveredEdge.getVertexCount();
		
		if (vertexCount == 2) {
			List<DrawableVertex> vertices = new ArrayList<DrawableVertex>(connectedVertices);
			
			int x1 = (int) vertices.get(0).getPosition().getX();
			int y1 = (int) vertices.get(0).getPosition().getY();
			int x2 = (int) vertices.get(1).getPosition().getX();
			int y2 = (int) vertices.get(1).getPosition().getY();
			
			drawBrokenLine(g, seperationLength, x1, y1, x2, y2);
		} else if (coveredEdge.getVertexCount() > 2) {
			Vector2d middle = new Vector2d();
			
			for (DrawableVertex vertex : connectedVertices) {
				middle = middle.add(vertex.getPosition());
			}
			
			middle = middle.div(coveredEdge.getVertexCount());
			
			for (DrawableVertex vertex : connectedVertices) {
				int x1 = (int) middle.getX();
				int y1 = (int) middle.getY();
				int x2 = (int) vertex.getPosition().getX();
				int y2 = (int) vertex.getPosition().getY();
				
				if (coveredEdge.isCrossover()) {
					drawBrokenLine(g, seperationLength, x1, y1, x2, y2);
				} else {
					g.drawLine(x1, y1, x2, y2);
				}
			}
		}
	}
	
	
	private static void drawBrokenLine(Graphics g, double seperationLength, int x1, int y1, int x2, int y2) {
		Vector2d a = new Vector2d(x1, y1);
		Vector2d b = new Vector2d(x2, y2);
		Vector2d line = b.sub(a);
		
		Vector2d seperationVector = line.normalize().mul(seperationLength);
		
		int devisions = (int) (line.length() / seperationLength);
		int devision = 0;
		Vector2d start = a;
		
		for (; devision < devisions; devision += 2) {
			int lx1 = (int) start.getX();
			int ly1 = (int) start.getY();
			int lx2 = (int) start.add(seperationVector).getX();
			int ly2 = (int) start.add(seperationVector).getY();
			
			g.drawLine(lx1, ly1, lx2, ly2);
			
			start = start.add(seperationVector.mul(2));
		}
		
		if (devision == devisions) {
			int lx1 = (int) start.getX();
			int ly1 = (int) start.getY();
			int lx2 = (int) b.getX();
			int ly2 = (int) b.getY();
			
			g.drawLine(lx1, ly1, lx2, ly2);
		}
	}
	
}