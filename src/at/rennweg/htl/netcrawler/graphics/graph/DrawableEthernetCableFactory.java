package at.rennweg.htl.netcrawler.graphics.graph;


import java.util.Set;

import at.andiwand.library.graphics.graph.DrawableEdge;
import at.andiwand.library.graphics.graph.DrawableEdgeFactory;
import at.andiwand.library.graphics.graph.DrawableVertex;
import at.andiwand.library.math.graph.Edge;



public class DrawableEthernetCableFactory extends DrawableEdgeFactory {
	
	@Override
	public DrawableEdge buildEdge(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		return new DrawableEthernetCable(coveredEdge, connectedVertices);
	}
	
}