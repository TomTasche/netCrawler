package at.netcrawler.util;

import java.awt.Dimension;


public class Settings {
	
	private boolean keepServerAlive;
	private int[] lastWindowSize;
	

	private Settings() {}
	
	
	public void setKeepServerAlive(boolean keepServerAlive) {
		this.keepServerAlive = keepServerAlive;
	}
	
	public boolean isKeepServerAlive() {
		return keepServerAlive;
	}
	
	public void setLastWindowSize(Dimension dimension) {
		lastWindowSize = new int[2];
		lastWindowSize[0] = dimension.width;
		lastWindowSize[1] = dimension.height;
	}
	
	public Dimension getLastWindowSize() {
		return new Dimension(lastWindowSize[0], lastWindowSize[1]);
	}
}
