package at.andiwand.library.graphics.graph2;

import java.awt.Graphics;

import javax.swing.JComponent;

import at.andiwand.library.math.graph.Edge;
import at.andiwand.library.math.graph.Graph;


public class JGraph extends JComponent {
	
	private static final long serialVersionUID = -312610458193450702L;
	
	
	
	private Graph<Object, Edge<Object>> graphModel;
	
	private boolean deformAbleEnabled;
	private boolean multiSelectEnabled;
	
	
	
	public JGraph() {}
	public <V, E extends Edge<V>> JGraph(Graph<V, E> graphModel) {
		setGraphModel(graphModel);
	}
	
	
	
	public Graph<Object, Edge<Object>> getGraphModel() {
		return graphModel;
	}
	
	public boolean isDeformAbleEnabled() {
		return deformAbleEnabled;
	}
	public boolean isMultiSelectEnabled() {
		return multiSelectEnabled;
	}
	
	
	@SuppressWarnings("unchecked")
	public <V, E extends Edge<V>> void setGraphModel(Graph<V, E> graphModel) {
		this.graphModel = (Graph<Object, Edge<Object>>) graphModel;
	}
	
	public void setDeformAbleEnabled(boolean deformAbleEnabled) {
		this.deformAbleEnabled = deformAbleEnabled;
	}
	public void setMultiSelectEnabled(boolean multiSelectEnabled) {
		this.multiSelectEnabled = multiSelectEnabled;
	}
	
	
	
	@Override
	protected void paintComponent(Graphics g) {
		
	}
	
}