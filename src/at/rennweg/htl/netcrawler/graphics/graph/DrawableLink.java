package at.rennweg.htl.netcrawler.graphics.graph;

import java.util.Set;

import at.andiwand.library.graphics.graph.DrawableEdge;
import at.andiwand.library.graphics.graph.DrawableVertex;
import at.andiwand.library.math.graph.Edge;


public abstract class DrawableLink extends DrawableEdge {
	
	public DrawableLink(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		super(coveredEdge, connectedVertices);
	}
	
}