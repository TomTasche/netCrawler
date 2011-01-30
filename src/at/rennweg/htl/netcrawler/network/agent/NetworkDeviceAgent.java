package at.rennweg.htl.netcrawler.network.agent;

import java.net.InetAddress;
import java.util.Set;

import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;
import at.rennweg.htl.netcrawler.network.graph.NetworkInterface;


public abstract class NetworkDeviceAgent {
	
	public abstract Set<NetworkInterface> fetchInterfaces();
	
	public abstract String fetchHostname();
	public abstract Set<InetAddress> fetchManagementAddresses();
	
	
	public NetworkDevice fetchAll() {
		return new NetworkDevice(this);
	}
	
}