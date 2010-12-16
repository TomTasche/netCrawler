package at.rennweg.htl.netcrawler.graphics.graph;

import graphics.graph.DrawableEdge;
import graphics.graph.DrawableEdgeFactory;
import graphics.graph.DrawableVertex;

import java.util.Set;

import math.graph.Edge;


public class DrawableEthernetCableFactory extends DrawableEdgeFactory {
	
	@Override
	public DrawableEdge buildEdge(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		return new DrawableEthernetCable(coveredEdge, connectedVertices);
	}
	
}