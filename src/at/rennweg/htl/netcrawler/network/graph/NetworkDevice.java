package at.rennweg.htl.netcrawler.network.graph;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;


public abstract class NetworkDevice {
	
	protected String name;
	protected InetAddress managementAddress;
	
	protected Set<NetworkInterface> interfaces;
	
	
	public NetworkDevice() {
		this(null, null);
	}
	public NetworkDevice(String name, InetAddress managementAddress) {
		this.name = name;
		this.managementAddress = managementAddress;
		
		interfaces = new HashSet<NetworkInterface>();
	}
	
	
	@Override
	public abstract boolean equals(Object obj);
	@Override
	public abstract int hashCode();
	
	
	public String getName() {
		return name;
	}
	public InetAddress getManagementAddress() {
		return managementAddress;
	}
	public Set<NetworkInterface> getInterfaces() {
		return new HashSet<NetworkInterface>(interfaces);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setManagementAddress(InetAddress managementAddress) {
		this.managementAddress = managementAddress;
	}
	public void setInterfaces(Set<NetworkInterface> interfaces) {
		this.interfaces = new HashSet<NetworkInterface>(interfaces);
	}
	
}