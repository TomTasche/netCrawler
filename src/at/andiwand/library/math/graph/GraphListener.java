package at.andiwand.library.math.graph;


public interface GraphListener<V, E extends Edge<V>> {
	
	public void vertexAdded(V vertex);
	public void edgeAdded(E edge);
	
	public void vertexRemoved(V vertex);
	public void edgeRemoved(E edge);
	
}