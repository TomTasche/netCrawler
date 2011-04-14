package at.rennweg.htl.netcrawler.test;

import javax.swing.UIManager;

import at.rennweg.htl.netcrawler.graphics.ui.CrawlerOptionPane;


public class TestCrawlerOptionPane {
	
	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		CrawlerOptionPane crawlerOptionPane = new CrawlerOptionPane(true);
		crawlerOptionPane.showOptionPane(null);
		
		System.exit(0);
	}
	
}