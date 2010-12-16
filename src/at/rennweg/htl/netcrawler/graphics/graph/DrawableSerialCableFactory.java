package at.rennweg.htl.netcrawler.graphics.graph;

import java.util.Set;

import math.graph.Edge;
import graphics.graph.DrawableEdge;
import graphics.graph.DrawableEdgeFactory;
import graphics.graph.DrawableVertex;


public class DrawableSerialCableFactory extends DrawableEdgeFactory {
	
	public DrawableSerialCableFactory() {}
	
	
	@Override
	public DrawableEdge buildEdge(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		return new DrawableSerialCable(coveredEdge, connectedVertices);
	}
	
}