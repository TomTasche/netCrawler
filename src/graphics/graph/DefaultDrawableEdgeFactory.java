package graphics.graph;

import java.util.Set;

import math.graph.Edge;


public class DefaultDrawableEdgeFactory extends DrawableEdgeFactory {
	
	@Override
	public DrawableEdge buildEdge(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		return new DefaultDrawableEdge(coveredEdge, connectedVertices);
	}
	
}