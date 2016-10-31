package at.netcrawler.ui.assistant;

import javax.swing.JFrame;
import javax.swing.UIManager;

import at.andiwand.library.component.JFrameUtil;


public class ConfigurationManagerLauncher {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Couldn't apply system look and feel.");
		}
		
		ConfigurationManager manager = new ConfigurationManager();
		manager.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JFrameUtil.centerFrame(manager);
		manager.setVisible(true);
	}
	
}