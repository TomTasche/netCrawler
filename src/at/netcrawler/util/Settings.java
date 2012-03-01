package at.netcrawler.util;

import java.awt.Dimension;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


public class Settings {
	
	private static final Preferences PREFERENCES = Preferences.userNodeForPackage(Settings.class);
	
	private static Dimension lastWindowSize;
	private static int lastView;
	private static String lastCrawl;
	
	static {
		int width = PREFERENCES.getInt("width", 0);
		int height = PREFERENCES.getInt("height", 0);
		if (width != 0 && height != 0) {
			lastWindowSize = new Dimension(width, height);
		}
		
		lastView = PREFERENCES.getInt("view", 0);
		
		lastCrawl = PREFERENCES.get("crawl", "");
	}
	
	public static void setLastWindowSize(Dimension dimension) {
		lastWindowSize = dimension;
	}
	
	public static Dimension getLastWindowSize() {
		return lastWindowSize;
	}
	
	public static int getLastView() {
		return lastView;
	}
	
	public static void setLastView(int lastView) {
		Settings.lastView = lastView;
	}
	
	public static String getLastCrawl() {
		return lastCrawl;
	}
	
	public static void setLastCrawl(String lastCrawl) {
		Settings.lastCrawl = lastCrawl;
	}
	
	public static void write() {
		PREFERENCES.putInt("width", lastWindowSize.width);
		PREFERENCES.putInt("height", lastWindowSize.height);
		PREFERENCES.putInt("view", lastView);
		PREFERENCES.put("crawl", lastCrawl);
		
		try {
			PREFERENCES.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
