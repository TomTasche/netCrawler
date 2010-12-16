package at.rennweg.htl.netcrawler.graphics.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import math.Rectangle;
import math.Vector2d;

import at.rennweg.htl.netcrawler.math.graph.NetworkDevice;
import graphics.graph.DrawableVertex;


public abstract class DrawableNetworkDevice extends DrawableVertex {
	
	public static final int DEFAULT_MAX_NAME_LENGTH = 10;
	
	public static final Font DEFUALT_FONT = new Font("monospaced", Font.PLAIN, 10);
	public static final Color DEFUALT_FONT_COLOR = Color.BLACK;
	
	
	private NetworkDevice coveredVertex;
	
	private int maxNameLength = DEFAULT_MAX_NAME_LENGTH;
	
	private Font font = DEFUALT_FONT;
	private Color fontColor = DEFUALT_FONT_COLOR;
	
	
	public DrawableNetworkDevice(Object coveredVertex) {
		super(coveredVertex);
		
		this.coveredVertex = (NetworkDevice) coveredVertex;
	}
	
	
	public NetworkDevice getCoveredVertex() {
		return coveredVertex;
	}
	
	@Override
	public abstract Rectangle drawingRect();
	
	public Font getFont() {
		return font;
	}
	
	
	@Override
	public boolean intersection(Vector2d point) {
		Rectangle drawingRect = drawingRect();
		
		if (drawingRect == null) return false;
		
		return drawingRect.intersection(point);
	}
	
	
	public abstract void drawDevice(Graphics g);
	
	@Override
	public void draw(Graphics g) {
		drawDevice(g);
		
		g.setFont(font);
		g.setColor(fontColor);
		
		String name = coveredVertex.getName();
		if (name.length() > maxNameLength) {
			name = name.substring(0, maxNameLength - 3);
			name += "...";
		}
		
		FontMetrics fontMetrics = g.getFontMetrics();
		Rectangle drawingRect = drawingRect();
		
		int x = (int) (drawingRect.getPosition().getX() - fontMetrics.charsWidth(name.toCharArray(), 0, name.length()) / 2);
		int y = (int) (drawingRect.bottom() + fontMetrics.getHeight());
		
		g.drawString(coveredVertex.getName(), x, y);
	}
	
}