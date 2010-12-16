package at.rennweg.htl.netcrawler.math.graph;

import java.net.InetAddress;


public abstract class NetworkDevice {
	
	protected String name;
	protected InetAddress managementAddress;
	
	
	public NetworkDevice() {
		this(null, null);
	}
	public NetworkDevice(String name, InetAddress managementAddress) {
		this.name = name;
		this.managementAddress = managementAddress;
	}
	
	
	public String getName() {
		return name;
	}
	public InetAddress getManagementAddress() {
		return managementAddress;
	}
	
}