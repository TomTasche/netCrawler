package at.andiwand.library.graphics.graph;

import java.awt.Graphics;
import java.awt.Image;


public class ImageVertex extends DrawableVertex {
	
	private static final long serialVersionUID = -2355289147277606329L;
	
	
	private Image image;
	
	public ImageVertex(Object coveredVertex, Image image) {
		super(coveredVertex);
		
		this.image = image;
	}
	
	@Override
	public void draw(Graphics g) {
		if (image == null) return;
		if (image.getWidth(null) < 0) return;
		
		g.drawImage(image, getX(), getY(), null);
	}
	
}