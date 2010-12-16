package graphics.graph;

import java.util.Set;

import math.graph.Edge;


public abstract class DrawableEdgeFactory {
	
	public abstract DrawableEdge buildEdge(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices);
	
}