package at.andiwand.library.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

import at.andiwand.library.math.MathUtil;
import at.andiwand.library.math.Vector2d;



public class GraphicsUtil {
	
	private GraphicsUtil() {}
	
	
	public static void drawLine(Graphics g, Vector2d a, Vector2d b) {
		g.drawLine((int) a.getX(), (int) a.getY(), (int) b.getX(), (int) b.getY());
	}
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
	
	public static void drawImage(Graphics g, Image img, Vector2d pos) {
		drawImage(g, img, pos, null);
	}
	public static void drawImage(Graphics g, Image img, Vector2d pos, ImageObserver imageObserver) {
		g.drawImage(img, (int) pos.getX(), (int) pos.getY(), imageObserver);
	}
	
	
	public static void translate(Graphics g, Vector2d vector) {
		g.translate((int) vector.getX(), (int) vector.getY());
	}
	public static void rotate(Graphics graphics, Vector2d vector) {
		Graphics2D g = (Graphics2D) graphics;
		g.rotate(MathUtil.atan2(vector));
	}
	
}