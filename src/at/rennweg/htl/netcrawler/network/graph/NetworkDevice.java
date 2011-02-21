package at.rennweg.htl.netcrawler.network.graph;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

import at.rennweg.htl.netcrawler.network.agent.NetworkDeviceAgent;


public class NetworkDevice {
	
	protected Set<NetworkInterface> interfaces;
	
	protected String hostname;
	protected Set<InetAddress> managementAddresses;
	
	
	public NetworkDevice() {
		this((String) null);
	}
	public NetworkDevice(String hostname) {
		this(hostname, new HashSet<InetAddress>());
	}
	public NetworkDevice(String hostname, Set<InetAddress> managementAddresses) {
		this(new HashSet<NetworkInterface>(), hostname, managementAddresses);
	}
	public NetworkDevice(Set<NetworkInterface> interfaces, String hostname, Set<InetAddress> managementAddresses) {
		this.interfaces = new HashSet<NetworkInterface>(interfaces);
		
		this.hostname = hostname;
		this.managementAddresses = new HashSet<InetAddress>(managementAddresses);
	}
	public NetworkDevice(NetworkDeviceAgent deviceAgent) {
		interfaces = deviceAgent.fetchInterfaces();
		
		hostname = deviceAgent.fetchHostname();
		managementAddresses = deviceAgent.fetchManagementAddresses();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		
		if (!(obj instanceof NetworkDevice)) return false;
		NetworkDevice device = (NetworkDevice) obj;
		
		return hostname.equals(device.hostname);
	}
	@Override
	public int hashCode() {
		return hostname.hashCode();
	}
	@Override
	public String toString() {
		return hostname;
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
	public String getHostname() {
		return hostname;
	}
	public Set<InetAddress> getManagementAddresses() {
		return managementAddresses;
	}
	
	public void setInterfaces(Set<NetworkInterface> interfaces) {
		this.interfaces = new HashSet<NetworkInterface>(interfaces);
		
		for (NetworkInterface networkInterface : this.interfaces) {
			networkInterface.parentDevice = this;
		}
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public void setManagementAddresses(Set<InetAddress> managementAddresses) {
		this.managementAddresses = new HashSet<InetAddress>(managementAddresses);
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