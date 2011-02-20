package at.andiwand.library.graphics;

import java.awt.Graphics;

import at.andiwand.library.math.Rectangle;



public interface Drawable {
	
	public Rectangle drawingRect();
	
	public void draw(Graphics g);
	
}