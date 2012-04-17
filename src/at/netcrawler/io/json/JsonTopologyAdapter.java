package at.netcrawler.io.json;

import at.netcrawler.network.topology.Topology;


public abstract class JsonTopologyAdapter<T extends Topology> extends
		JsonAdapter<T> {
	
	public abstract T getInstance();
	
}