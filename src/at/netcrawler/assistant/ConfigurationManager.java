package at.netcrawler.assistant;

import javax.swing.JFrame;
import javax.swing.UIManager;

import at.andiwand.library.component.JFrameUtil;
import at.netcrawler.ui.graphical.main.BatchManager;

public class ConfigurationManager {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Couldn't apply system look and feel.");
		}
		
		BatchManager manager = new BatchManager();
		manager.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JFrameUtil.centerFrame(manager);
		manager.setVisible(true);
	}
}
