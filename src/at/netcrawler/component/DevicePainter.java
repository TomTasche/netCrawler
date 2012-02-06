package at.netcrawler.component;

import java.awt.Dimension;
import java.awt.Graphics;

import at.andiwand.library.component.GraphViewerVertex;


public interface DevicePainter {
	
	public Dimension getSize();
	
	public void paint(Graphics g, GraphViewerVertex vertex);
	
}