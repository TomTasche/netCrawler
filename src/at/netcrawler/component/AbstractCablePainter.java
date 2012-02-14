package at.netcrawler.component;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Iterator;
import java.util.Set;


public abstract class AbstractCablePainter implements CablePainter {
	
	public void paint(Graphics g, TopologyViewerCable cable) {
		Set<TopologyViewerDevice> devices = cable.getDevices();
		Point[] points = new Point[devices.size()];
		
		Iterator<TopologyViewerDevice> iterator = devices.iterator();
		for (int i = 0; i < points.length; i++) {
			points[i] = iterator.next().getMiddle();
		}
		
		paint(g, points);
	}
	
	public abstract void paint(Graphics g, Point[] points);
	
}