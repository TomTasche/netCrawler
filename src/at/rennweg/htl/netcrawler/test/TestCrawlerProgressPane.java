package at.rennweg.htl.netcrawler.test;

import at.rennweg.htl.netcrawler.graphics.ui.CrawlerProgressPane;


public class TestCrawlerProgressPane {
	
	public static void main(String[] args) {
		CrawlerProgressPane progressPane = new CrawlerProgressPane();
		progressPane.showProgress(null);
		
		System.exit(0);
	}
	
}