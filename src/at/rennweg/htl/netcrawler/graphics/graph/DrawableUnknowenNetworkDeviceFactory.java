package at.rennweg.htl.netcrawler.graphics.graph;

import graphics.graph.DrawableVertex;
import graphics.graph.DrawableVertexFactory;


public class DrawableUnknowenNetworkDeviceFactory extends DrawableVertexFactory {
	
	public DrawableUnknowenNetworkDeviceFactory() {}
	
	
	@Override
	public DrawableVertex buildVertex(Object coveredVertex) {
		return new DrawableUnknowenNetworkDevice(coveredVertex);
	}
	
}