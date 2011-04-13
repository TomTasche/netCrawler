package at.andiwand.library.graphics.graph;

import java.io.Serializable;


/**
 * 
 * The root of all graph layouts. <br>
 * A graph layout positions the drawable vertices of a <code>JGraph</code> on
 * the panel.
 * 
 * @author Andreas Stefl
 *
 */
public abstract class GraphLayout implements Serializable {
	
	private static final long serialVersionUID = 1385694165715939938L;
	
	
	
	
	/**
	 * The <code>JGraph</code> object.
	 */
	protected JGraph jGraph;
	
	
	
	/**
	 * Initiates the <code>GraphLayout</code> object with the given parameters.
	 * 
	 * @param jGraph the <code>JGraph</code> object.
	 */
	public GraphLayout(JGraph jGraph) {
		this.jGraph = jGraph;
	}
	
	
	
	/**
	 * Returns the <code>JGraph</code> object.
	 * 
	 * @return the <code>JGraph</code> object.
	 */
	public JGraph getJGraph() {
		return jGraph;
	}
	
	
	/**
	 * Repositions all vertices on the panel and fills the position cache with
	 * new data.
	 */
	public abstract void reposition();
	
}