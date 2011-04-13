package at.andiwand.library.graphics.graph;

import java.awt.Graphics;

import javax.swing.ImageIcon;


public class ImageVertex extends DrawableVertex {
	
	private static final long serialVersionUID = -2355289147277606329L;
	
	
	private ImageIcon image;
	
	public ImageVertex(Object coveredVertex, ImageIcon image) {
		super(coveredVertex);
		
		this.image = image;
		
		setSize(image.getIconWidth(), image.getIconHeight());
	}
	
	@Override
	public void draw(Graphics g) {
		if (image == null) return;
		if (image.getIconWidth() < 0) return;
		
		g.drawImage(image.getImage(), getX(), getY(), image.getImageObserver());
	}
	
}