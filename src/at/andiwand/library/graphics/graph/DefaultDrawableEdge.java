package at.andiwand.library.graphics.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Set;

import at.andiwand.library.math.graph.Edge;


public class DefaultDrawableEdge extends DrawableEdge {
	
	public static final Color DEFAULT_COLOR = Color.BLACK;
	
	
	private Color color = DEFAULT_COLOR;
	
	public DefaultDrawableEdge(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		super(coveredEdge, connectedVertices);
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		
		if (coveredEdge.getVertexCount() > 1) {
			Point middle = new Point();
			
			for (DrawableVertex vertex : connectedVertices) {
				middle.x += vertex.getCenter().x;
				middle.y += vertex.getCenter().y;
			}
			
			int vertexCount = coveredEdge.getVertexCount();
			middle.x /= vertexCount;
			middle.y /= vertexCount;
			
			for (DrawableVertex vertex : connectedVertices) {
				g.drawLine(middle.x, middle.y,
						vertex.getCenter().x, vertex.getCenter().y);
			}
		}
	}
	
}