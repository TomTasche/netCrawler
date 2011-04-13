package at.rennweg.htl.netcrawler.graphics.graph;

import java.awt.Graphics;

import javax.swing.ImageIcon;


//TODO: remove rectangle user
public class DrawableImagedDevice extends DrawableNetworkDevice {
	
	private static final long serialVersionUID = -5738064527761224525L;
	
	
	private ImageIcon image;
	
	public DrawableImagedDevice(Object coveredVertex, ImageIcon image) {
		super(coveredVertex);
		
		this.image = image;
		
		setSize(image.getIconWidth(), image.getIconHeight());
	}
	
	@Override
	public void drawDevice(Graphics g) {
		g.drawImage(image.getImage(), getX(), getY(), image.getImageObserver());
	}
	
}