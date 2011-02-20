package at.andiwand.packettracer;


public abstract class NetworkInterface {
	
	protected NetworkDevice device;
	
	private String name;
	
	
	public NetworkInterface(String name) {
		this.name = name;
	}
	
	
	@Override
	public String toString() {
		return name;
	}
	
}