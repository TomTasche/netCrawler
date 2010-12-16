package graphics;

import java.awt.Graphics;

import math.Rectangle;


public interface Drawable {
	
	public Rectangle drawingRect();
	
	public void draw(Graphics g);
	
}