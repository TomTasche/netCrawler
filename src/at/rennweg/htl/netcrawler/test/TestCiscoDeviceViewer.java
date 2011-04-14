package at.rennweg.htl.netcrawler.test;

import javax.swing.UIManager;

import at.rennweg.htl.netcrawler.graphics.ui.CiscoDeviceViewer;
import at.rennweg.htl.netcrawler.network.graph.CiscoRouter;


public class TestCiscoDeviceViewer {
	
	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		CiscoRouter router = new CiscoRouter("Router");
		router.setShowVersion("yey! i'm a version that is shown! :D");
		
		CiscoDeviceViewer deviceViewer = new CiscoDeviceViewer(router);
		deviceViewer.showDialog(null);
		
		System.exit(0);
	}
	
}