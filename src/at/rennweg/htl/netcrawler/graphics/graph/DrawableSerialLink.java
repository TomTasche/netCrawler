package at.rennweg.htl.netcrawler.graphics.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;

import at.andiwand.library.graphics.graph.DrawableSingeEdge;
import at.andiwand.library.graphics.graph.DrawableVertex;
import at.andiwand.library.math.graph.Edge;
import at.rennweg.htl.netcrawler.network.graph.SerialLink;


//TODO: remove vector user
public class DrawableSerialLink extends DrawableSingeEdge {
	
	public static final Dimension DEFUALT_FLASH_SIZE = new Dimension(6, 10);
	
	public static final Color DEFAULT_COLOR = Color.RED;
	
	
	
	private SerialLink coveredEdge;
	
	private Dimension flashSize = DEFUALT_FLASH_SIZE;
	private Color color = DEFAULT_COLOR;
	
	
	public DrawableSerialLink(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		super(coveredEdge, connectedVertices);
		
		if (!(((Edge<?>) coveredEdge) instanceof SerialLink))
			throw new IllegalArgumentException("coveredEdge is no instance of SerialCable");
		
		this.coveredEdge = (SerialLink) (Edge<?>) coveredEdge;
	}
	
	
	public SerialLink getSerialCable() {
		return coveredEdge;
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		
		int dx = vertexA.getCenterX() - vertexB.getCenterX();
		int dy = vertexA.getCenterY() - vertexB.getCenterY();
		double theta = Math.atan2(-dy, -dx);
		g2.translate(vertexA.getCenterX(), vertexA.getCenterY());
		g2.rotate(theta);
		
		int length = (int) vertexA.getLocation().distance(
				vertexB.getLocation());
		int centerX = length / 2;
		
		int x1 = 0;
		int y1 = 0;
		int x2 = centerX + flashSize.width / 2;
		int y2 = -flashSize.height / 2;
		g2.drawLine(x1, y1, x2, y2);
		
		x1 = x2;
		y1 = y2;
		x2 = centerX - flashSize.width / 2;
		y2 = flashSize.height / 2;
		g2.drawLine(x1, y1, x2, y2);
		
		x1 = x2;
		y1 = y2;
		x2 = length;
		y2 = 0;
		g2.drawLine(x1, y1, x2, y2);
	}
	
}