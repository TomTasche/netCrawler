package at.netcrawler.assistant;

import javax.swing.JFrame;
import javax.swing.UIManager;

import at.andiwand.library.component.JFrameUtil;
import at.netcrawler.ui.graphical.BatchExecutor;

public class ConfigurationExecutor {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Couldn't apply system look and feel.");
		}
		
		BatchExecutor executor = new BatchExecutor();
		executor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JFrameUtil.centerFrame(executor);
		executor.setVisible(true);
	}
}