package at.rennweg.htl.netcrawler.network.agent;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Set;

import at.andiwand.library.util.SimpleRemoteExecutor;
import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;
import at.rennweg.htl.netcrawler.network.graph.NetworkInterface;


public abstract class SimpleNetworkDeviceAgent {
	
	protected final SimpleRemoteExecutor executor;
	
	
	
	public SimpleNetworkDeviceAgent(SimpleRemoteExecutor executor) {
		this.executor = executor;
	}
	
	
	
	public abstract Set<NetworkInterface> fetchInterfaces() throws IOException;
	
	public abstract String fetchHostname() throws IOException;
	public abstract Set<InetAddress> fetchManagementAddresses() throws IOException;
	
	
	public NetworkDevice fetchAll() throws IOException {
		NetworkDevice result = new NetworkDevice();
		
		fetchAll(result);
		
		return result;
	}
	public void fetchAll(NetworkDevice device) throws IOException {
		device.setHostname(fetchHostname());
		device.setInterfaces(fetchInterfaces());
		device.setManagementAddresses(fetchManagementAddresses());
	}
	
}