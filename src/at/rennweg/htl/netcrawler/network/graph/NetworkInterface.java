package at.rennweg.htl.netcrawler.network.graph;


public abstract class NetworkInterface {
	
	protected String name;
	
	
	public NetworkInterface() {
		this(null);
	}
	public NetworkInterface(String name) {
		this.name = name;
	}
	
	
	@Override
	public abstract boolean equals(Object obj);
	@Override
	public abstract int hashCode();
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}