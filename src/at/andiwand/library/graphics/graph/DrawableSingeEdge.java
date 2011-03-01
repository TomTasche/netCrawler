package at.andiwand.library.graphics.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import at.andiwand.library.math.graph.Edge;


public abstract class DrawableSingeEdge extends DrawableEdge {
	
	protected DrawableVertex vertexA;
	protected DrawableVertex vertexB;
	
	
	public DrawableSingeEdge(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		super(coveredEdge, connectedVertices);
		
		if (connectedVertices.size() > 2)
			throw new IllegalArgumentException("cannot take more than 2 edges");
		
		List<DrawableVertex> list = new ArrayList<DrawableVertex>(connectedVertices);
		vertexA = list.get(0);
		if (list.size() > 1) vertexB = list.get(1);
		else vertexB = vertexA;
	}
	
	
	public DrawableVertex getVertexA() {
		return vertexA;
	}
	public DrawableVertex getVertexB() {
		return vertexB;
	}
	
}