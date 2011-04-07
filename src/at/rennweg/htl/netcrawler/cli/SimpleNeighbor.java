package at.rennweg.htl.netcrawler.cli;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;


public class SimpleNeighbor {
	
	private NetworkDevice sourceDevice;
	
	private String name;
	private List<InetAddress> managementAddresses;
	private String sourceInterfaceName;
	private String interfaceName;
	
	
	
	public String toString() {
		return name;
	}
	
	
	public NetworkDevice getSourceDevice() {
		return sourceDevice;
	}
	public String getName() {
		return name;
	}
	public List<InetAddress> getManagementAddresses() {
		return Collections.unmodifiableList(managementAddresses);
	}
	public String getSourceInterfaceName() {
		return sourceInterfaceName;
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	
	public void setSourceDevice(NetworkDevice sourceDevice) {
		this.sourceDevice = sourceDevice;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setManagementAddresses(List<InetAddress> managementAddresses) {
		this.managementAddresses = new ArrayList<InetAddress>(managementAddresses);
	}
	public void setSourceInterfaceName(String sourceInterfaceName) {
		this.sourceInterfaceName = sourceInterfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	
}