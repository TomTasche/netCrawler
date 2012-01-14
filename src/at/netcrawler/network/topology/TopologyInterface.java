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
		String name = (String) networkInterface.getValue(NetworkInterface.NAME);
		
		return device.toString() + ": " + name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof TopologyInterface)) return false;
		TopologyInterface interfaze = (TopologyInterface) obj;
		
		String nameA = (String) networkInterface.getValue(NetworkInterface.NAME);
		String nameB = (String) interfaze.networkInterface.getValue(NetworkInterface.NAME);
		
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
	
}