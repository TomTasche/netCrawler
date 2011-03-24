package at.rennweg.htl.netcrawler.graphics.graph;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


//TODO: remove rectangle user
public class DrawableImagedDevice extends DrawableNetworkDevice {
	
	private BufferedImage image;
	
	public DrawableImagedDevice(Object coveredVertex, BufferedImage image) {
		super(coveredVertex);
		
		this.image = image;
		
		setSize(image.getWidth(), image.getHeight());
	}
	
	@Override
	public void drawDevice(Graphics g) {
		g.drawImage(image, getX(), getY(), null);
	}
	
}