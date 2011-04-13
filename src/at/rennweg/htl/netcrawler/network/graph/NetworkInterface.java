package at.rennweg.htl.netcrawler.network.graph;

import java.io.Serializable;


public class NetworkInterface implements Serializable {
	
	private static final long serialVersionUID = 6480214695817280672L;
	
	
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