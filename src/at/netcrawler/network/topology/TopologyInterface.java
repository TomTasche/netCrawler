package at.netcrawler.network.topology;

import at.netcrawler.network.model.NetworkInterface;


public class TopologyInterface {
	
	private final NetworkInterface networkInterface;
	private TopologyDevice device;
	
	public TopologyInterface(NetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
	}
	
	@Override
	public String toString() {
		if (device == null) return getName();
		return device.toString() + ": " + getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof TopologyInterface)) return false;
		TopologyInterface interfaze = (TopologyInterface) obj;
		
		if (device == null) return getName().equals(
				interfaze.getName());
		return device.equals(interfaze.device) && getName().equals(
				interfaze.getName());
	}
	
	@Override
	public int hashCode() {
		if (device == null) return getName().hashCode();
		return device.hashCode() ^ getName().hashCode();
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
	
	void setDevice(TopologyDevice device) {
		this.device = device;
	}
	
}