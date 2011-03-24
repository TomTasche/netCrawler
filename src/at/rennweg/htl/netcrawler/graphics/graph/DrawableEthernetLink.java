package at.rennweg.htl.netcrawler.graphics.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import at.andiwand.library.graphics.GraphicsUtil;
import at.andiwand.library.graphics.graph.DrawableEdge;
import at.andiwand.library.graphics.graph.DrawableVertex;
import at.andiwand.library.math.graph.Edge;
import at.rennweg.htl.netcrawler.network.graph.EthernetLink;


//TODO: remove vector user
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
	public void draw(Graphics g) {
		g.setColor(color);
		
		List<DrawableVertex> vertices = new ArrayList<DrawableVertex>(connectedVertices);
		
		Point a = vertices.get(0).getCenter();
		Point b = vertices.get(1).getCenter();
		
		if (coveredEdge.isCrossover()) {
			GraphicsUtil.drawBrokenLine(g, seperationLength, a, b);
		} else {
			GraphicsUtil.drawLine(g, a, b);
		}
	}
	
}