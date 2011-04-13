package at.andiwand.library.graphics.graph;

import javax.swing.ImageIcon;


public class ImageVertexFactory extends DrawableVertexFactory {
	
	private static final long serialVersionUID = -4191056523872250030L;
	
	
	private ImageIcon image;
	
	public ImageVertexFactory(ImageIcon image) {
		this.image = image;
	}
	
	@Override
	public DrawableVertex buildVertex(Object coveredVertex) {
		return new ImageVertex(coveredVertex, image);
	}
	
}