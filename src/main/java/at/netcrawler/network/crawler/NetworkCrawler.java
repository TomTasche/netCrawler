package at.netcrawler.network.crawler;

import java.io.IOException;

import at.netcrawler.network.topology.Topology;


public interface NetworkCrawler {
	
	public void crawl(Topology topology) throws IOException;
	
}