package at.andiwand.library.graphics;

import java.awt.Graphics;


/**
 * 
 * A simple interface that should be invoked by a <code>Drawable</object> object.
 * 
 * @author Andreas Stefl
 * 
 */
public interface Drawable {
	
	/**
	 * Draws this <code>Drawable</object> object on the given
	 * <code>Graphics</code> object.
	 * 
	 * @param g the <code>Graphics</code> object where it should be drawn.
	 */
	public void draw(Graphics g);
	
}