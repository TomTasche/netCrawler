package at.andiwand.library.graphics.graph;

import java.util.Set;

import at.andiwand.library.math.graph.Edge;



public abstract class DrawableEdgeFactory {
	
	public abstract DrawableEdge buildEdge(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices);
	
}