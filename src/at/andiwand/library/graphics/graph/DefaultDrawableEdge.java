package at.andiwand.library.graphics.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;

import at.andiwand.library.math.Vector2d;
import at.andiwand.library.math.graph.Edge;


public class DefaultDrawableEdge extends DrawableEdge {
	
	public static final Color DEFAULT_COLOR = Color.BLACK;
	
	
	private Color color = DEFAULT_COLOR;
	
	
	public DefaultDrawableEdge(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		super(coveredEdge, connectedVertices);
	}
	
	
	@Override
	public boolean intersection(Vector2d point) {
		return false;
	}
	
	
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		
		if (coveredEdge.getVertexCount() > 1) {
			Vector2d middle = new Vector2d();
			
			for (DrawableVertex vertex : connectedVertices) {
				middle = middle.add(vertex.getCenterPosition());
			}
			
			middle = middle.div(coveredEdge.getVertexCount());
			
			for (DrawableVertex vertex : connectedVertices) {
				int x1 = (int) middle.getX();
				int y1 = (int) middle.getY();
				int x2 = (int) vertex.getCenterPosition().getX();
				int y2 = (int) vertex.getCenterPosition().getY();
				
				g.drawLine(x1, y1, x2, y2);
			}
		}
	}
	
}