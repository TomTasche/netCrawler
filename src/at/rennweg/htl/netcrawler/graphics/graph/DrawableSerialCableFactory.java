package at.rennweg.htl.netcrawler.graphics.graph;

import java.util.Set;

import at.andiwand.library.graphics.graph.DrawableEdge;
import at.andiwand.library.graphics.graph.DrawableEdgeFactory;
import at.andiwand.library.graphics.graph.DrawableVertex;
import at.andiwand.library.math.graph.Edge;



public class DrawableSerialCableFactory extends DrawableEdgeFactory {
	
	public DrawableSerialCableFactory() {}
	
	
	@Override
	public DrawableEdge buildEdge(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		return new DrawableSerialCable(coveredEdge, connectedVertices);
	}
	
}