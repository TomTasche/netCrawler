package graphics.graph;


public abstract class GraphLayout {
	
	protected JGraph jGraph;
	
	
	public GraphLayout(JGraph jGraph) {
		this.jGraph = jGraph;
	}
	
	
	public JGraph getJGraph() {
		return jGraph;
	}
	
	
	public abstract void reposition();
	
	public abstract void positionUpdate();
	
}