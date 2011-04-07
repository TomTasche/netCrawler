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
		
		if (parentDevice == null) return name.equals(networkInterface.name);
		return parentDevice.equals(networkInterface.parentDevice) &&
			name.equals(networkInterface.name);
	}
	@Override
	public int hashCode() {
		if (parentDevice == null) return name.hashCode();
		return parentDevice.hashCode() ^ name.hashCode();
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