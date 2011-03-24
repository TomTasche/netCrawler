package at.andiwand.library.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;


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
	 * <code>Graphics</code> object.
	 * 
	 * @param g the <code>Graphics</code> object where it should be drawn.
	 * @param a the start point of the line.
	 * @param b the end point of the line.
	 * @see java.awt.Graphics#drawLine(int, int, int, int)
	 */
	public static void drawLine(Graphics g, Point a, Point b) {
		g.drawLine(a.x, a.y, b.x, b.y);
	}
	
	/**
	 * Draws a broken line from <code>a</code> to <code>b</code> with a
	 * <code>seperationLength</code> on the given <code>Graphics</code>
	 * object.
	 * 
	 * @param g the <code>Graphics</code> object where it should be drawn.
	 * @param seperationLength the length of a single line and the distance
	 * between them.
	 * @param a the start point of the line.
	 * @param b the end point of the line.
	 * @see at.andiwand.library.graphics.GraphicsUtil#drawLine(Graphics, Vector2d, Vector2d)
	 */
	public static void drawBrokenLine(Graphics g, double seperationLength, Point a, Point b) {
		Graphics2D g2 = (Graphics2D) g.create();
		
		double length = a.distance(b);
		double theta = Math.atan2(b.y - a.y, b.x - a.x);
		
		g2.translate(a.x, a.y);
		g2.rotate(theta);
		
		int devisions = (int) (length / seperationLength);
		int devision = 0;
		double x = 0;
		
		for (; devision < devisions; devision += 2) {
			g2.drawLine((int) x, 0, (int) (x + seperationLength), 0);
			
			x += seperationLength * 2;
		}
		
		if (devision == devisions)
			g2.drawLine((int) x, 0, (int) length, 0);
	}
	
	
	/**
	 * Draws a rectangle from the <code>Rectangle</code> object. <br>
	 * Node: the <code>double</code> components are bounded back to an
	 * <code>int</code>.
	 * 
	 * @param g the <code>Graphics</code> object where it should be drawn.
	 * @param rectangle the <code>Rectangle</code> object.
	 * @see java.awt.Graphics#drawRect(int, int, int, int)
	 */
	public static void drawRect(Graphics g, Rectangle rectangle) {
		g.drawRect((int) rectangle.x, (int) rectangle.y,
				(int) rectangle.width, (int) rectangle.height);
	}
	
	/**
	 * Draws a rectangle from the diagonal <code>vertexA</code> to
	 * <code>vertexB</code> on the given <code>Graphics</code> object. <br>
	 * Node: the <code>double</code> components are bounded back to an
	 * <code>int</code>.
	 * 
	 * @param g the <code>Graphics</code> object where it should be drawn.
	 * @param vertexA one of the diagonal vertices on the rectangle.
	 * @param vertexB one of the diagonal vertices on the rectangle.
	 * @see java.awt.Graphics#drawRect(int, int, int, int)
	 */
	public static void drawRect(Graphics g, Point pointA, Point pointB) {
		int x = Math.min(pointA.x, pointB.x);
		int y = Math.min(pointA.y, pointB.y);
		int width = Math.max(pointA.x, pointB.x) - x;
		int height = Math.max(pointA.y, pointB.y) - y;
		
		g.drawRect(x, y, width, height);
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
	public static void drawImage(Graphics g, Image img, Point pos) {
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
	public static void drawImage(Graphics g, Image img, Point pos, ImageObserver imageObserver) {
		g.drawImage(img, pos.x, pos.y, imageObserver);
	}
	
	
	
	/**
	 * Translates the <code>Graphics</code> object by the given translation
	 * vector.
	 * 
	 * @param g the <code>Graphics</code> object where it should be drawn.
	 * @param vector the relative translation vector.
	 */
	public static void translate(Graphics g, Point vector) {
		g.translate(vector.x, vector.y);
	}
	
	
	
	
	/**
	 * Prevents creating an instance of this class.
	 */
	private GraphicsUtil() {}
	
}