package at.andiwand.library.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;


public class JFrameUtil {
	
	private JFrameUtil() {}
	
	public static void centerFrame(Window window) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = window.getSize();
		
		int x = (screenSize.width - frameSize.width) >> 1;
		int y = (screenSize.height - frameSize.height) >> 1;
		
		window.setLocation(x, y);
	}
	public static void centerFrame(Window window, Window parent) {
		Dimension frameSize = window.getSize();
		
		int x = parent.getX() + ((parent.getWidth() - frameSize.width) >> 1);
		int y = parent.getY() + ((parent.getHeight() - frameSize.height) >> 1);
		
		window.setLocation(x, y);
	}
	
}