package at.rennweg.htl.netcrawler.graphics.graph;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import math.Rectangle;
import math.Vector2d;


public class DrawableUnknowenNetworkDevice extends DrawableNetworkDevice {
	
	public static final String IMAGE_NAME = "unknown.png";
	public static final URL IMAGE_URL = DrawableCiscoRouter.class.getResource(IMAGE_NAME);
	
	private static Image image;
	private static final Object syncImage = new Object();
	
	
	public DrawableUnknowenNetworkDevice(Object coveredVertex) {
		super(coveredVertex);
		
		if (image == null) {
			synchronized (syncImage) {
				if (image == null) {
					new Thread() {
						public void run() {
							image = Toolkit.getDefaultToolkit().getImage(IMAGE_URL);
							
							try {
								while(image.getWidth(null) < 0) Thread.sleep(100);
							} catch (InterruptedException e) {}
							
							syncImage.notifyAll();
						}
					}.start();
				}
			}
		}
	}
	
	
	@Override
	public Rectangle drawingRect() {
		if (image == null) {
			synchronized (syncImage) {
				try {
					syncImage.wait();
				} catch (InterruptedException e) {}
			}
		}
		
		return new Rectangle(getPosition(), new Vector2d(
				image.getWidth(null),
				image.getHeight(null)
		));
	}
	
	
	@Override
	public void drawDevice(Graphics g) {
		if (image == null) {
			synchronized (syncImage) {
				try {
					syncImage.wait();
				} catch (InterruptedException e) {}
			}
		}
		
		Rectangle drawingRect = drawingRect();
		
		int x = (int) drawingRect.left();
		int y = (int) drawingRect.top();
		
		g.drawImage(image, x, y, null);
	}
	
}