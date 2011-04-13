package at.andiwand.library.graphics.graph;


public class DefaultDrawableVertexFactory extends DrawableVertexFactory {
	
	private static final long serialVersionUID = 9121447549893540332L;
	
	@Override
	public DrawableVertex buildVertex(Object coveredVertex) {
		return new DefaultDrawableVertex(coveredVertex);
	}
	
}