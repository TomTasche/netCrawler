package at.rennweg.htl.netcrawler.graphics.graph;

import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;

import at.andiwand.library.graphics.graph.DrawableVertex;
import at.andiwand.library.graphics.graph.DrawableVertexFactory;


public class DrawableImagedDeviceFactory extends DrawableVertexFactory {
	
	private static final long serialVersionUID = -7844340372997502571L;
	
	
	private ImageIcon image;
	
	public DrawableImagedDeviceFactory(String defaultImage) throws IOException {
		this(DrawableImagedDeviceFactory.class.getResource(defaultImage));
	}
	public DrawableImagedDeviceFactory(URL imageUrl) throws IOException {
		image = new ImageIcon(imageUrl);
	}
	
	@Override
	public DrawableVertex buildVertex(Object coveredVertex) {
		return new DrawableImagedDevice(coveredVertex, image);
	}
	
}