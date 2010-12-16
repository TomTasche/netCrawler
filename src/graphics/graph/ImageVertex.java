package graphics.graph;


import java.awt.Graphics;
import java.awt.Image;

import math.Rectangle;
import math.Vector2d;


public class ImageVertex extends DrawableVertex {
	
	private Image image;
	
	
	public ImageVertex(Object coveredVertex, Image image) {
		super(coveredVertex);
		
		this.image = image;
	}
	
	
	@Override
	public Rectangle drawingRect() {
		return new Rectangle(getPosition(), new Vector2d(
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