package at.netcrawler.network;

public enum Capability {
	
	HUB("Hub"),
	SWITCH("Switch"),
	ROUTER("Router"),
	FIREWALL("Firewall");
	
	private final String name;
	
	private Capability(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
}