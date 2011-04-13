package at.andiwand.library.graphics.graph;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import at.andiwand.library.graphics.Drawable;
import at.andiwand.library.math.graph.Edge;


/**
 * 
 * The root of all drawable edges. <br>
 * It covers and offers access an <code>Edge</code> object.
 * 
 * @author Andreas Stefl
 * 
 */
public abstract class DrawableEdge implements Drawable, Serializable {
	
	private static final long serialVersionUID = 8315957929885794161L;
	
	
	
	
	private final Edge<Object> coveredEdge;
	
	
	/**
	 * The parent graph.
	 */
	protected JGraph graph;
	
	private final Set<DrawableVertex> connectedVertices;
	
	
	
	/**
	 * Initiates the <code>DrawableEdge</code> object with the given parameters.
	 * 
	 * @param coveredEdge the covered <code>Edge</code> object.
	 * @param connectedVertices the connected vertices.
	 */
	public DrawableEdge(Edge<Object> coveredEdge, Set<DrawableVertex> connectedVertices) {
		if (connectedVertices.isEmpty())
			throw new IllegalArgumentException("The vertex set is empty!");
		
		this.coveredEdge = coveredEdge;
		
		this.connectedVertices = Collections.unmodifiableSet(connectedVertices);
	}
	
	
	
	/**
     * Returns the containing <code>Edge</code> object as a string.
     * 
     * @return the containing <code>Edge</code> object as a string.
     */
	@Override
	public String toString() {
		return "DrawableEdge[" + coveredEdge.toString() + "]";
	}
	
	
	/**
	 * Returns the covered <code>Edge</code> object.
	 * 
	 * @return the covered <code>Edge</code> object.
	 */
	public Edge<Object> getCoveredEdge() {
		return coveredEdge;
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
	 * Returns an unmodifiable set of the connected vertices.
	 * 
	 * @return an unmodifiable set of the connected vertices. 
	 */
	public Set<DrawableVertex> getConnectedVertices() {
		return connectedVertices;
	}
	
}