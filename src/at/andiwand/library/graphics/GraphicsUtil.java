package at.andiwand.library.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

import at.andiwand.library.math.MathUtil;
import at.andiwand.library.math.Vector2d;


/**
 * 
 * A simple static class that combines the <code>Graphics</code> methods with
 * the <code>Vector</code> and <code>Matrix</code> classes.
 * Also, it contains simple methods to draw complex graphics.
 * 
 * @author Andreas Stefl
 * 
 */
public class GraphicsUtil {
	
	/**
	 * Draws a line from <code>a</code> to <code>b</code> on the given
	 * <code>Graphics</code> object. <br>
	 * Node: the <code>double</code> components are bounded back to an
	 * <code>int</code>.
	 * 
	 * @param g the <code>Graphics</code> object where it should be drawn.
	 * @param a the start point of the line.
	 * @param b the end point of the line.
	 * @see java.awt.Graphics#drawLine(int, int, int, int)
	 */
	public static void drawLine(Graphics g, Vector2d a, Vector2d b) {
		g.drawLine((int) a.getX(), (int) a.getY(), (int) b.getX(), (int) b.getY());
	}
	
	/**
	 * Draws a broken line from <code>a</code> to <code>b</code> with a
	 * <code>seperationLength</code> on the given <code>Graphics</code>
	 * object. <br>
	 * Node: the <code>double</code> components are bounded back to an
	 * <code>int</code>.
	 * 
	 * @param g the <code>Graphics</code> object where it should be drawn.
	 * @param seperationLength the length of a single line and the distance
	 * between them.
	 * @param a the start point of the line.
	 * @param b the end point of the line.
	 * @see at.andiwand.library.graphics.GraphicsUtil#drawLine(Graphics, Vector2d, Vector2d)
	 */
	public static void drawBrokenLine(Graphics g, double seperationLength, Vector2d a, Vector2d b) {
		Vector2d line = b.sub(a);
		
		Vector2d seperationVector = line.normalize().mul(seperationLength);
		
		int devisions = (int) (line.length() / seperationLength);
		int devision = 0;
		Vector2d start = a;
		
		for (; devision < devisions; devision += 2) {
			drawLine(g, start, start.add(seperationVector));
			
			start = start.add(seperationVector.mul(2));
		}
		
		if (devision == devisions)
			drawLine(g, start, b);
	}
	
	
	/**
	 * Draws the given <code>Image</code> to the position <code>pos</code> on
	 * the <code>Graphics</code> object <code>g</code>. <br>
	 * Node: the <code>double</code> components are bounded back to an
	 * <code>int</code>.
	 * 
	 * @param g the <code>Graphics</code> object where it should be drawn.
	 * @param img the <code>Image</code> that should be drawn.
	 * @param pos the left-top position of the image.
	 * @see at.andiwand.library.graphics.GraphicsUtil#drawImage(Graphics, Image, Vector2d, ImageObserver)
	 */
	public static void drawImage(Graphics g, Image img, Vector2d pos) {
		drawImage(g, img, pos, null);
	}
	
	/**
	 * Draws the given <code>Image</code> to the position <code>pos</code> on
	 * the <code>Graphics</code> object <code>g</code>. <br>
	 * Node: the <code>double</code> components are bounded back to an
	 * <code>int</code>.
	 * 
	 * @param g the <code>Graphics</code> object where it should be drawn.
	 * @param img the <code>Image</code> that should be drawn.
	 * @param pos the left-top position of the image.
	 * @param imageObserver object to be notified as more of the image is
	 * converted.
	 */
	public static void drawImage(Graphics g, Image img, Vector2d pos, ImageObserver imageObserver) {
		g.drawImage(img, (int) pos.getX(), (int) pos.getY(), imageObserver);
	}
	
	
	/**
	 * Translates the <code>Graphics</code> object by the given translation
	 * vector.
	 * 
	 * @param g the <code>Graphics</code> object where it should be drawn.
	 * @param vector the relative translation vector.
	 */
	public static void translate(Graphics g, Vector2d vector) {
		g.translate((int) vector.getX(), (int) vector.getY());
	}
	
	/**
	 * Rotates the <code>Graphics</code> object to the given direction.
	 * 
	 * @param g the <code>Graphics</code> object where it should be drawn.
	 * @param vector the absolute rotation vector.
	 */
	public static void rotate(Graphics g, Vector2d vector) {
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(MathUtil.atan2(vector));
	}
	
	
	
	/**
	 * Prevents creating an instance of this class.
	 */
	private GraphicsUtil() {}
	
}