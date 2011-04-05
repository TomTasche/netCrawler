package at.rennweg.htl.netcrawler.network.crawler;

import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;


public abstract class NetworkCrawler {
	
	public abstract void crawl(NetworkGraph networkGraph) throws Exception;
	
}