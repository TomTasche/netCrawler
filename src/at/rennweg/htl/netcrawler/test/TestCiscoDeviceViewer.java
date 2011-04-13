package at.rennweg.htl.netcrawler.test;

import javax.swing.UIManager;

import at.andiwand.library.util.JFrameUtil;
import at.rennweg.htl.netcrawler.graphics.ui.CiscoDeviceViewer;
import at.rennweg.htl.netcrawler.network.graph.CiscoRouter;


public class TestCiscoDeviceViewer {
	
	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		CiscoRouter router = new CiscoRouter("Router");
		router.setShowVersion("yey! i'm a version that is shown! :D");
		
		CiscoDeviceViewer deviceViewer = new CiscoDeviceViewer(router);
		deviceViewer.setSize(500, 500);
		JFrameUtil.centerFrame(deviceViewer);
		deviceViewer.setVisible(true);
		deviceViewer.setDefaultCloseOperation(CiscoDeviceViewer.EXIT_ON_CLOSE);
	}
	
}