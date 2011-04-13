package at.rennweg.htl.netcrawler.graphics.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import at.andiwand.library.graphics.graph.DrawableVertex;
import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;


public abstract class DrawableNetworkDevice extends DrawableVertex {
	
	private static final long serialVersionUID = 4395291222310277003L;
	
	public static final int DEFAULT_MAX_NAME_LENGTH = 10;
	
	public static final Font DEFUALT_FONT = new Font("monospaced", Font.PLAIN, 10);
	public static final Color DEFUALT_FONT_COLOR = Color.BLACK;
	
	
	protected final NetworkDevice coveredVertex;
	
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
	
	
	public abstract void drawDevice(Graphics g);
	
	@Override
	public void draw(Graphics g) {
		drawDevice(g);
		
		g.setFont(font);
		g.setColor(fontColor);
		
		String name = coveredVertex.getHostname();
		if (name.length() > maxNameLength) {
			name = name.substring(0, maxNameLength - 3);
			name += "...";
		}
		
		FontMetrics fontMetrics = g.getFontMetrics();
		
		int x = (int) (getCenterX() - fontMetrics.charsWidth(
				name.toCharArray(), 0, name.length()) / 2);
		int y = getY() + getHeight() + fontMetrics.getHeight();
		
		g.drawString(coveredVertex.getHostname(), x, y);
	}
	
}