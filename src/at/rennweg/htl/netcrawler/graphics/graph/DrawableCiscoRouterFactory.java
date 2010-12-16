package at.rennweg.htl.netcrawler.graphics.graph;

import graphics.graph.DrawableVertex;
import graphics.graph.DrawableVertexFactory;


public class DrawableCiscoRouterFactory extends DrawableVertexFactory {
	
	public DrawableCiscoRouterFactory() {}
	
	
	@Override
	public DrawableVertex buildVertex(Object coveredVertex) {
		return new DrawableCiscoRouter(coveredVertex);
	}
	
}