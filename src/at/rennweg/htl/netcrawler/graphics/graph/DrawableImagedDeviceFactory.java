package at.rennweg.htl.netcrawler.graphics.graph;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import at.andiwand.library.graphics.graph.DrawableVertex;
import at.andiwand.library.graphics.graph.DrawableVertexFactory;


public class DrawableImagedDeviceFactory extends DrawableVertexFactory {
	
	private BufferedImage image;
	
	
	public DrawableImagedDeviceFactory(String defaultImage) throws IOException {
		this(DrawableImagedDeviceFactory.class.getResource(defaultImage));
	}
	public DrawableImagedDeviceFactory(URL imageUrl) throws IOException {
		image = ImageIO.read(imageUrl);
	}
	
	
	@Override
	public DrawableVertex buildVertex(Object coveredVertex) {
		return new DrawableImagedDevice(coveredVertex, image);
	}
	
}