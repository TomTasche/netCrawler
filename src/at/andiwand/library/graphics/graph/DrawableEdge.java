package at.andiwand.library.graphics.graph;

import java.awt.event.MouseAdapter;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.graphics.Drawable;
import at.andiwand.library.graphics.Intersectable;
import at.andiwand.library.math.graph.Edge;


public abstract class DrawableEdge implements Drawable, Intersectable {
	
	protected Edge<Object> coveredEdge;
	
	protected Set<DrawableVertex> connectedVertices;
	
	
	public DrawableEdge(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		if (connectedVertices.isEmpty())
			throw new IllegalArgumentException("vertex set is empty");
		
		this.coveredEdge = coveredEdge;
		
		this.connectedVertices = connectedVertices;
	}
	
	
	@Override
	public String toString() {
		return coveredEdge.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	
	public Edge<Object> getCoveredEdge() {
		return coveredEdge;
	}
	
	public Set<DrawableVertex> getConnectedVertices() {
		return new HashSet<DrawableVertex>(connectedVertices);
	}
	
	public MouseAdapter getMouseAdapter() {
		return null;
	}
	
}