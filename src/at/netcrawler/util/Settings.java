package at.netcrawler.util;

import java.awt.Dimension;


public class Settings {
	
	private Dimension lastWindowSize;
	private int lastWindowState;
	
	private Settings() {}
	
	public void setLastWindowSize(Dimension dimension) {
		this.lastWindowSize = dimension;
	}
	
	public Dimension getLastWindowSize() {
		return lastWindowSize;
	}
	
	public int getLastWindowState() {
		return lastWindowState;
	}
	
	public void setLastWindowState(int lastWindowState) {
		this.lastWindowState = lastWindowState;
	}
}
