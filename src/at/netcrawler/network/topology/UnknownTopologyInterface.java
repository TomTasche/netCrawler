package at.netcrawler.network.topology;

import at.netcrawler.network.model.NetworkInterface;


public class UnknownTopologyInterface extends TopologyInterface {
	
	private static final String NAME = "?";
	
	private final Object identifier = new Object();
	
	public UnknownTopologyInterface(NetworkInterface networkInterface) {
		super(networkInterface);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof UnknownTopologyInterface)) return false;
		UnknownTopologyInterface interfaze = (UnknownTopologyInterface) obj;
		
		return identifier.equals(interfaze.identifier);
	}
	
	@Override
	public int hashCode() {
		return identifier.hashCode();
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
}