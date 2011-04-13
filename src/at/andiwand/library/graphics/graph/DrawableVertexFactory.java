package at.andiwand.library.graphics.graph;

import java.io.Serializable;


/**
 * 
 * The root of all vertex factories. <br>
 * An instance of this class can create a <code>DrawableVertex</code> object
 * from the given vertex object.
 * 
 * @author Andreas Stefl
 *
 */
public abstract class DrawableVertexFactory implements Serializable {
	
	private static final long serialVersionUID = -2700302051703457L;
	
	/**
	 * Builds a new <code>DrawableVertex</code> object from the given vertex
	 * object.
	 * 
	 * @param coveredVertex the vertex object.
	 * @return the new <code>DrawableVertex</code> object.
	 */
	public abstract DrawableVertex buildVertex(Object coveredVertex);
	
}