package at.andiwand.library.graphics.graph;

import java.awt.event.MouseAdapter;

import at.andiwand.library.graphics.Drawable;
import at.andiwand.library.graphics.Intersectable;
import at.andiwand.library.math.Rectangle;
import at.andiwand.library.math.Vector2d;


/**
 * 
 * The root of all drawable edges. It covers and offers access an
 * <code>Edge</code> object.
 * 
 * @author Andreas Stefl
 * 
 */
public abstract class DrawableVertex implements Drawable, Intersectable {
	
	/**
	 * The covered object.
	 */
	private Object coveredVertex;
	
	/**
	 * The centered position of the vertex.
	 */
	private Vector2d middle;
	
	
	
	
	/**
	 * Initiates the <code>DrawableVertex</code> object with the given
	 * parameters.
	 * 
	 * @param coveredVertex the covered object.
	 */
	public DrawableVertex(Object coveredVertex) {
		this(coveredVertex, new Vector2d());
	}
	
	/**
	 * Initiates the <code>DrawableVertex</code> object with the given
	 * parameters.
	 * 
	 * @param coveredVertex the covered object.
	 * @param middle the centered position of the vertex.
	 */
	public DrawableVertex(Object coveredVertex, Vector2d middle) {
		this.coveredVertex = coveredVertex;
		
		this.middle = middle;
	}
	
	
	
	
	/**
     * Returns the containing object as a string.
     * 
     * @return the containing object as a string.
     */
	@Override
	public String toString() {
		return coveredVertex.toString();
	}
	
	
	
	/**
	 * Returns the bounds of the <code>DrawableVertex</code> object.
	 * 
	 * @return the bounds of the <code>DrawableVertex</code> object.
	 */
	public abstract Rectangle drawingRect();
	
	/**
	 * Returns the covered object.
	 * 
	 * @return the covered object.
	 */
	public Object getCoveredVertex() {
		return coveredVertex;
	}
	
	/**
	 * Returns the centered position of the vertex.
	 * 
	 * @return the centered position of the vertex.
	 */
	public Vector2d getCenterPosition() {
		return middle;
	}
	
	/**
	 * Returns the <code>MouseAdapter</code> object.
	 * 
	 * @return the <code>MouseAdapter</code> object.
	 */
	public MouseAdapter getMouseAdapter() {
		return null;
	}
	
	
	/**
	 * Sets the centered position of the vertex to the given parameters.
	 * 
	 * @param middle the centered position of the vertex.
	 */
	public void setCenterPosition(Vector2d middle) {
		this.middle = middle;
	}
	
}