package at.rennweg.htl.netcrawler.graphics.graph;

import graphics.graph.DrawableVertex;
import graphics.graph.DrawableVertexFactory;


public class DrawableCiscoSwitchFactory extends DrawableVertexFactory {
	
	public DrawableCiscoSwitchFactory() {}
	
	
	@Override
	public DrawableVertex buildVertex(Object coveredVertex) {
		return new DrawableCiscoSwitch(coveredVertex);
	}
	
}