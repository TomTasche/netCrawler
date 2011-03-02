package at.andiwand.library.graphics.graph;

import java.awt.Graphics;
import java.awt.Image;

import at.andiwand.library.math.Rectangle;
import at.andiwand.library.math.Vector2d;


public class ImageVertex extends DrawableVertex {
	
	private static final long serialVersionUID = -2355289147277606329L;
	
	
	
	private Image image;
	
	
	
	public ImageVertex(Object coveredVertex, Image image) {
		super(coveredVertex);
		
		this.image = image;
	}
	
	
	
	@Override
	public Rectangle drawingRect() {
		return new Rectangle(getCenterPosition(), new Vector2d(
				image.getWidth(null),
				image.getHeight(null)
		));
	}
	
	@Override
	public boolean intersection(Vector2d point) {
		Rectangle drawingRect = drawingRect();
		
		return drawingRect.intersection(point);
	}
	
	
	@Override
	public void draw(Graphics g) {
		if (image == null) return;
		if (image.getWidth(null) < 0) return;
		
		Rectangle drawingRect = drawingRect();
		
		int x = (int) drawingRect.left();
		int y = (int) drawingRect.top();
		
		g.drawImage(image, x, y, null);
	}
	
}