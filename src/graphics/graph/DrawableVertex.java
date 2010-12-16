package graphics.graph;

import java.awt.event.MouseAdapter;

import math.Vector2d;

import graphics.Drawable;
import graphics.Intersectable;


public abstract class DrawableVertex implements Drawable, Intersectable {
	
	private Object coveredVertex;
	
	private Vector2d position;
	
	
	public DrawableVertex(Object coveredVertex) {
		this(coveredVertex, new Vector2d());
	}
	public DrawableVertex(Object coveredVertex, Vector2d position) {
		this.coveredVertex = coveredVertex;
		
		this.position = position;
	}
	
	
	@Override
	public String toString() {
		return coveredVertex.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	
	public Object getCoveredVertex() {
		return coveredVertex;
	}
	
	public Vector2d getPosition() {
		return position;
	}
	
	public MouseAdapter getMouseAdapter() {
		return null;
	}
	
	
	public void setPosition(Vector2d position) {
		this.position = position;
	}
	
}