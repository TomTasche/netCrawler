package at.rennweg.htl.netcrawler.test;

import at.andiwand.library.util.JFrameUtil;
import at.rennweg.htl.netcrawler.graphics.ui.CiscoDeviceViewer;
import at.rennweg.htl.netcrawler.graphics.ui.MainFrame;


public class TestMainFrame {
	
	public static void main(String[] args) throws Throwable {
		//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		MainFrame mainFrame = new MainFrame();
		mainFrame.setSize(700, 600);
		JFrameUtil.centerFrame(mainFrame);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(CiscoDeviceViewer.EXIT_ON_CLOSE);
	}
	
}