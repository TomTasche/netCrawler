package at.rennweg.htl.netcrawler.network.crawler;

import at.rennweg.htl.netcrawler.network.graph.NetworkCable;
import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;


public abstract class NetworkCrawler {
	
	public abstract void crawl(NetworkGraph<? extends NetworkDevice, ? extends NetworkCable<? extends NetworkDevice>> networkGraph);
	
}