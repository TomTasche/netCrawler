package at.rennweg.htl.graphic.graph;


public abstract class JVertexFactory<SV, DV extends JVertex> {
	
	public abstract DV buildJVertex(Class<SV> clazz, SV sourceVertex);
	
}