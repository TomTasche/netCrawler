package at.netcrawler.ui.component;

import java.awt.Graphics;
import java.awt.Point;

import at.andiwand.library.graphics.GraphicsUtil;


public class UnknownCablePainter extends AbstractCablePainter {
	
	@Override
	public void paint(Graphics g, Point[] points) {
		GraphicsUtil graphicsUtil = new GraphicsUtil(g);
		
		if (points.length == 2) {
			graphicsUtil.drawLine(points[0], points[1]);
		} else {
			Point middle = new Point();
			
			for (int j = 0; j < points.length; j++) {
				middle.x += points[j].x;
				middle.y += points[j].y;
			}
			
			middle.x /= points.length;
			middle.y /= points.length;
			
			for (int j = 0; j < points.length; j++) {
				graphicsUtil.drawLine(points[j], middle);
			}
		}
	}
	
}