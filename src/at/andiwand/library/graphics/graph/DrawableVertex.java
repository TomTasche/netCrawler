package at.andiwand.library.graphics.graph;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import at.andiwand.library.graphics.Drawable;
import at.andiwand.library.graphics.Intersectable;


/**
 * 
 * The root of all drawable edges. <br>
 * It covers and offers access an <code>Edge</code> object.
 * 
 * @author Andreas Stefl
 * 
 */
public abstract class DrawableVertex implements Drawable, Intersectable, Serializable {
	
	private static final long serialVersionUID = 9177408732393196980L;
	
	
	
	
	
	private Object coveredVertex;
	
	
	/**
	 * The parent graph.
	 */
	protected JGraph graph;
	
	private Rectangle bounds;
	
	
	
	
	/**
	 * Initiates the <code>DrawableVertex</code> object with the given
	 * parameters.
	 * 
	 * @param coveredVertex the covered object.
	 */
	public DrawableVertex(Object coveredVertex) {
		this.coveredVertex = coveredVertex;
		
		bounds = new Rectangle();
	}
	
	
	
	
	/**
     * Returns the containing object as a string.
     * 
     * @return the containing object as a string.
     */
	@Override
	public String toString() {
		return "DrawableVertex[" + coveredVertex.toString() + "]";
	}
	
	
	
	/**
	 * Returns the covered object.
	 * 
	 * @return the covered object.
	 */
	public Object getCoveredVertex() {
		return coveredVertex;
	}
	
	/**
	 * Returns the associated <code>JGraph</code> object.
	 * 
	 * @return the associated <code>JGraph</code> object.
	 */
	public JGraph getGraph() {
		return graph;
	}
	
	/**
	 * Returns the x-location of the vertex.
	 * 
	 * @return the x-location of the vertex.
	 */
	public int getX() {
		return bounds.x;
	}
	
	/**
	 * Returns the y-location of the vertex.
	 * 
	 * @return the y-location of the vertex.
	 */
	public int getY() {
		return bounds.y;
	}
	
	/**
	 * Returns the location of the vertex.
	 * 
	 * @return the location of the vertex.
	 */
	public Point getLocation() {
		return bounds.getLocation();
	}
	
	/**
	 * Returns the size of the vertex.
	 * 
	 * @return the size of the vertex.
	 */
	public Dimension getSize() {
		return bounds.getSize();
	}
	
	/**
	 * Returns the width of the vertex.
	 * 
	 * @return the width of the vertex.
	 */
	public int getWidth() {
		return bounds.width;
	}
	
	/**
	 * Returns the height of the vertex.
	 * 
	 * @return the height of the vertex.
	 */
	public int getHeight() {
		return bounds.height;
	}
	
	public double getDiagonal() {
		int w = bounds.width;
		int h = bounds.height;
		return Math.sqrt(w * w + h * h);
	}
	
	/**
	 * Returns the bounds of the <code>DrawableVertex</code> object.
	 * 
	 * @return the bounds of the <code>DrawableVertex</code> object.
	 */
	public Rectangle getBounds() {
		return new Rectangle(bounds);
	}
	
	/**
	 * Returns the centered position of the vertex.
	 * 
	 * @return the centered position of the vertex.
	 */
	public Point getCenter() {
		return new Point(bounds.x + bounds.width / 2,
				bounds.y + bounds.height / 2);
	}
	
	/**
	 * Returns the centered x-position of the vertex.
	 * 
	 * @return the centered x-position of the vertex.
	 */
	public int getCenterX() {
		return bounds.x + bounds.width / 2;
	}
	
	/**
	 * Returns the centered y-position of the vertex.
	 * 
	 * @return the centered y-position of the vertex.
	 */
	public int getCenterY() {
		return bounds.y + bounds.height / 2;
	}
	
	
	/**
	 * Sets the location of the vertex on the given point.
	 * 
	 * @param point the new location of the vertex.
	 */
	public void setLocation(int x, int y) {
		bounds.setLocation(x, y);
	}
	
	/**
	 * Sets the location of the vertex on the given point.
	 * 
	 * @param point the new location of the vertex.
	 */
	public void setLocation(Point point) {
		bounds.setLocation(point);
	}
	
	/**
	 * Sets the size of the vertex to the given dimension.
	 * 
	 * @param size the new size of the vertex.
	 */
	public void setSize(int width, int height) {
		bounds.width = width;
		bounds.height = height;
	}
	
	/**
	 * Sets the size of the vertex to the given dimension.
	 * 
	 * @param size the new size of the vertex.
	 */
	public void setSize(Dimension size) {
		bounds.setSize(size);
	}
	
	/**
	 * Sets the bounds of the vertex to the given rectangle.
	 * 
	 * @param rectangle the new bounds of the vertex.
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = new Rectangle(bounds);
	}
	
	/**
	 * Sets the centered position of the vertex on the given point.
	 * 
	 * @param center the new centered position of the vertex.
	 */
	public void setCenter(int x, int y) {
		bounds.x = x - bounds.width / 2;
		bounds.y = y - bounds.height / 2;
	}
	
	/**
	 * Sets the centered position of the vertex on the given point.
	 * 
	 * @param center the new centered position of the vertex.
	 */
	public void setCenter(Point center) {
		setCenter(center.x, center.y);
	}
	
	
	
	/**
	 * Tests an intersection of the object with the given <code>point</code>.
	 * 
	 * @param point the possible intersection point.
	 * @return <code>true</code> if the point intersects the object.
	 */
	@Override
	public boolean intersects(Point point) {
		return bounds.contains(point);
	}
	
}