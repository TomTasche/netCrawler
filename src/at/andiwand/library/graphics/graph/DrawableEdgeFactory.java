package at.andiwand.library.graphics.graph;

import java.util.Set;

import at.andiwand.library.math.graph.Edge;


/**
 * 
 * The root of all edges factories. <br>
 * An instance of this class can create a <code>DrawableEdge</code> object from
 * the given <code>Edge</code> object.
 * 
 * @author Andreas Stefl
 *
 */

//TODO: new concept
public abstract class DrawableEdgeFactory {
	
	/**
	 * Builds a new <code>DrawableEdge</code> object from the given
	 * <code>Edge</code> object.
	 * 
	 * @param coveredEdge the <code>Edge</code> object.
	 * @param connectedVertices the connected vertices.
	 * @return the new <code>DrawableEdge</code> object.
	 */
	public abstract DrawableEdge buildEdge(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices);
	
}