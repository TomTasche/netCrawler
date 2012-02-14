package at.netcrawler.component;

import java.awt.Dimension;
import java.awt.Point;

import at.andiwand.library.component.AbstractGraphLayout;
import at.andiwand.library.component.GraphViewer;
import at.andiwand.library.component.GraphViewerVertex;


public class CrapGraphLayout extends AbstractGraphLayout {
	
	private Point nextSpawn = new Point(10, 10);
	private Dimension spawnDistance = new Dimension(100, 100);
	
	public CrapGraphLayout(GraphViewer viewer) {
		super(viewer);
	}
	
	@Override
	protected void addViewerVertexImpl(GraphViewerVertex vertex) {
		vertex.setPosition(nextSpawn);
		nextSpawn.x += spawnDistance.width;
		nextSpawn.y += spawnDistance.height;
	}
	
}