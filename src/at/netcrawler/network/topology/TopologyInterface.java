package at.netcrawler.network.topology;

import at.netcrawler.network.model.NetworkInterface;


public class TopologyInterface {
	
	private final NetworkInterface networkInterface;
	
	private final TopologyDevice device;
	
	public TopologyInterface(NetworkInterface networkInterface,
			TopologyDevice device) {
		this.networkInterface = networkInterface;
		this.device = device;
	}
	
	@Override
	public String toString() {
		return device.toString() + ": " + getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof TopologyInterface)) return false;
		TopologyInterface interfaze = (TopologyInterface) obj;
		
		String nameA = getName();
		String nameB = interfaze.getName();
		
		return device.equals(interfaze.device) && nameA.equals(nameB);
	}
	
	@Override
	public int hashCode() {
		String name = (String) networkInterface.getValue(NetworkInterface.NAME);
		if (name == null) return 0;
		
		if (device == null) return name.hashCode();
		return device.hashCode() ^ name.hashCode();
	}
	
	public NetworkInterface getNetworkInterface() {
		return networkInterface;
	}
	
	public TopologyDevice getDevice() {
		return device;
	}
	
	public String getName() {
		return (String) networkInterface.getValue(NetworkInterface.NAME);
	}
	
}