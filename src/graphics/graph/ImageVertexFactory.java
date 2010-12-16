package graphics.graph;

import java.awt.Image;



public class ImageVertexFactory extends DrawableVertexFactory {
	
	private Image image;
	
	
	public ImageVertexFactory(Image image) {
		this.image = image;
	}
	
	
	@Override
	public DrawableVertex buildVertex(Object coveredVertex) {
		return new ImageVertex(coveredVertex, image);
	}
	
}