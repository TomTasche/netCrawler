package at.netcrawler.component;

import java.awt.Dimension;
import java.awt.Point;

import at.andiwand.library.component.AbstractGraphLayout;
import at.andiwand.library.component.GraphViewer;
import at.andiwand.library.component.GraphViewerVertex;


public class CrapGraphLayout extends AbstractGraphLayout {
	
	private GraphViewerVertex last;
	private Dimension spawnDistance = new Dimension(50, 50);
	
	public CrapGraphLayout(GraphViewer viewer) {
		super(viewer);
	}
	
	@Override
	protected void addViewerVertexImpl(GraphViewerVertex vertex) {
		Point point;
		if (last == null) {
			point = new Point(10, 10);
		} else {
			point = last.getPosition();
			point.x += spawnDistance.width;
			point.y += spawnDistance.height;
		}
		vertex.setPosition(point);
		last = vertex;
	}
	
}