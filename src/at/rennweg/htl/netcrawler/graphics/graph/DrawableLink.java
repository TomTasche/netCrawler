package at.rennweg.htl.netcrawler.graphics.graph;

import java.util.Set;

import at.andiwand.library.graphics.graph.DrawableEdge;
import at.andiwand.library.graphics.graph.DrawableVertex;
import at.andiwand.library.math.graph.Edge;


public abstract class DrawableLink extends DrawableEdge {
	
	private static final long serialVersionUID = 4558873709534849331L;
	
	public DrawableLink(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		super(coveredEdge, connectedVertices);
	}
	
}