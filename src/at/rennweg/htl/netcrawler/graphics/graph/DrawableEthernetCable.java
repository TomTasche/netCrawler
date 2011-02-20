package at.rennweg.htl.netcrawler.graphics.graph;


import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import at.andiwand.library.graphics.GraphicsUtil;
import at.andiwand.library.graphics.graph.DrawableEdge;
import at.andiwand.library.graphics.graph.DrawableVertex;
import at.andiwand.library.math.Rectangle;
import at.andiwand.library.math.Vector2d;
import at.andiwand.library.math.graph.Edge;
import at.rennweg.htl.netcrawler.network.graph.EthernetCable;


public class DrawableEthernetCable extends DrawableEdge {
	
	public static final Color DEFAULT_COLOR = Color.BLACK;
	public static final double DEFAULT_SEPERATION_LENGTH = 10;
	
	
	private EthernetCable coveredEdge;
	
	private Color color = DEFAULT_COLOR;
	private double seperationLength = DEFAULT_SEPERATION_LENGTH;
	
	
	public DrawableEthernetCable(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		super(coveredEdge, connectedVertices);
		
		if (!(((Edge<?>) coveredEdge) instanceof EthernetCable))
			throw new IllegalArgumentException("coveredEdge is no instance of EthernetCable");
		
		this.coveredEdge = (EthernetCable) (Edge<?>) coveredEdge;
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
			
			Vector2d a = vertices.get(0).getPosition();
			Vector2d b = vertices.get(1).getPosition();
			
			if (coveredEdge.isCrossover()) {
				GraphicsUtil.drawBrokenLine(g, seperationLength, a, b);
			} else {
				GraphicsUtil.drawLine(g, a, b);
			}
		} else if (coveredEdge.getVertexCount() > 2) {
			Vector2d middle = new Vector2d();
			
			for (DrawableVertex vertex : connectedVertices) {
				middle = middle.add(vertex.getPosition());
			}
			
			middle = middle.div(coveredEdge.getVertexCount());
			
			for (DrawableVertex vertex : connectedVertices) {
				Vector2d b = vertex.getPosition();
				
				if (coveredEdge.isCrossover()) {
					GraphicsUtil.drawBrokenLine(g, seperationLength, middle, b);
				} else {
					GraphicsUtil.drawLine(g, middle, b);
				}
			}
		}
	}
	
}