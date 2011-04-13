package at.andiwand.library.graphics.graph;

import java.util.Set;

import at.andiwand.library.math.graph.Edge;


public class DefaultDrawableEdgeFactory extends DrawableEdgeFactory {
	
	private static final long serialVersionUID = 6080736124335521451L;
	
	@Override
	public DrawableEdge buildEdge(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		return new DefaultDrawableEdge(coveredEdge, connectedVertices);
	}
	
}