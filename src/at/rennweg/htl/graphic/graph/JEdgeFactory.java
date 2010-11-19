package at.rennweg.htl.graphic.graph;

import math.graph.Edge;


public abstract class JEdgeFactory<SE extends Edge<?>, DE extends JEdge> {
	
	public abstract DE buildJEdge(Class<SE> clazz, SE sourceEdge);
	
}