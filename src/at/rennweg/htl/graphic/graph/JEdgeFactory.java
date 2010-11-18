package at.rennweg.htl.graphic.graph;

import at.rennweg.htl.math.graph.Edge;


public abstract class JEdgeFactory<SE extends Edge<?>, DE extends JEdge> {
	
	public abstract DE buildJEdge(Class<SE> clazz, SE sourceEdge);
	
}