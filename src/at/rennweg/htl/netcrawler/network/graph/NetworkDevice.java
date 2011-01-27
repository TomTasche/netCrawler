package at.rennweg.htl.netcrawler.network.graph;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;


public class NetworkDevice {
	
	protected Set<NetworkInterface> interfaces;
	
	protected String name;
	protected InetAddress managementAddress;
	
	
	public NetworkDevice() {
		this(null, null);
	}
	public NetworkDevice(String name, InetAddress managementAddress) {
		interfaces = new HashSet<NetworkInterface>();
		
		this.name = name;
		this.managementAddress = managementAddress;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		
		if (!(obj instanceof NetworkDevice)) return false;
		NetworkDevice device = (NetworkDevice) obj;
		
		return name.equals(device.name);
	}
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	@Override
	public String toString() {
		return name;
	}
	
	
	public Set<NetworkInterface> getInterfaces() {
		return new HashSet<NetworkInterface>(interfaces);
	}
	public NetworkInterface getInterface(String name) {
		for (NetworkInterface networkInterface : interfaces) {
			if (networkInterface.name.equals(name)) return networkInterface;
		}
		
		return null;
	}
	public String getName() {
		return name;
	}
	public InetAddress getManagementAddress() {
		return managementAddress;
	}
	
	public void setInterfaces(Set<NetworkInterface> interfaces) {
		this.interfaces = new HashSet<NetworkInterface>(interfaces);
		
		for (NetworkInterface networkInterface : this.interfaces) {
			networkInterface.parentDevice = this;
		}
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setManagementAddress(InetAddress managementAddress) {
		this.managementAddress = managementAddress;
	}
	
	
	public synchronized void addInterface(NetworkInterface networkInterface) {
		if (networkInterface.parentDevice != null)
			networkInterface.parentDevice.removeInterface(networkInterface);
		
		interfaces.add(networkInterface);
		networkInterface.parentDevice = this;
	}
	
	public synchronized void removeInterface(NetworkInterface networkInterface) {
		if (!interfaces.contains(networkInterface)) return;
		
		interfaces.remove(networkInterface);
		networkInterface.parentDevice = null;
	}
	
}