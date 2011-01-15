package at.rennweg.htl.netcrawler.graphics.graph;

import graphics.GraphicsUtil;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import math.Rectangle;
import math.Vector2d;


public class DrawableCiscoSwitch extends DrawableNetworkDevice {
	
	public static final String IMAGE_NAME = "switch.png";
	public static final URL IMAGE_URL = DrawableCiscoSwitch.class.getResource(IMAGE_NAME);
	
	private static Image image;
	private static final Object syncImage = new Object();
	
	
	public DrawableCiscoSwitch(Object coveredVertex) {
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
							
							synchronized (syncImage) {
								syncImage.notifyAll();
							}
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
		
		GraphicsUtil.drawImage(g, image, drawingRect.leftTop());
	}
	
}