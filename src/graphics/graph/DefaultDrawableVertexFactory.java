package graphics.graph;


public class DefaultDrawableVertexFactory extends DrawableVertexFactory {
	
	@Override
	public DrawableVertex buildVertex(Object coveredVertex) {
		return new DefaultDrawableVertex(coveredVertex);
	}
	
}