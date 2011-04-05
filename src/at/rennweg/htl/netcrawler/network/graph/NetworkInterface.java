package at.rennweg.htl.netcrawler.network.graph;


public class NetworkInterface {
	
	protected NetworkDevice parentDevice;
	
	protected String name;
	
	
	public NetworkInterface(String name) {
		this.name = name;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		
		if (!(obj instanceof NetworkInterface)) return false;
		NetworkInterface networkInterface = (NetworkInterface) obj;
		
		return parentDevice.equals(networkInterface.parentDevice) && name.equals(networkInterface.name);
	}
	@Override
	public int hashCode() {
		int result = name.hashCode();
		
		if (parentDevice != null) result ^= parentDevice.hashCode();
		
		return result;
	}
	@Override
	public String toString() {
		return name;
	}
	
	
	public NetworkDevice getParentDevice() {
		return parentDevice;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}