package at.rennweg.htl.netcrawler.graphics.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import at.andiwand.library.graphics.GraphicsUtil;
import at.andiwand.library.graphics.graph.DrawableEdge;
import at.andiwand.library.graphics.graph.DrawableVertex;
import at.andiwand.library.math.Vector2d;
import at.andiwand.library.math.graph.Edge;
import at.rennweg.htl.netcrawler.network.graph.EthernetLink;


public class DrawableEthernetLink extends DrawableEdge {
	
	public static final Color DEFAULT_COLOR = Color.BLACK;
	public static final double DEFAULT_SEPERATION_LENGTH = 10;
	
	
	private EthernetLink coveredEdge;
	
	private Color color = DEFAULT_COLOR;
	private double seperationLength = DEFAULT_SEPERATION_LENGTH;
	
	
	public DrawableEthernetLink(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		super(coveredEdge, connectedVertices);
		
		if (!(((Edge<?>) coveredEdge) instanceof EthernetLink))
			throw new IllegalArgumentException("coveredEdge is no instance of EthernetCable");
		
		this.coveredEdge = (EthernetLink) (Edge<?>) coveredEdge;
	}
	
	
	@Override
	public boolean intersection(Vector2d point) {
		return false;
	}
	
	
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		
		List<DrawableVertex> vertices = new ArrayList<DrawableVertex>(connectedVertices);
		
		Vector2d a = vertices.get(0).getCenterPosition();
		Vector2d b = vertices.get(1).getCenterPosition();
		
		if (coveredEdge.isCrossover()) {
			GraphicsUtil.drawBrokenLine(g, seperationLength, a, b);
		} else {
			GraphicsUtil.drawLine(g, a, b);
		}
	}
	
}