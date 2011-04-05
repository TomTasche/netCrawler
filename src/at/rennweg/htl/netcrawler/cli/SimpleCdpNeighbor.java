package at.rennweg.htl.netcrawler.cli;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;


public class SimpleCdpNeighbor {
	
	private String name;
	private List<InetAddress> managementAddresses;
	private String sourceInterfaceName;
	private String interfaceName;
	
	
	
	public String toString() {
		return name;
	}
	
	
	public String getName() {
		return name;
	}
	public List<InetAddress> getManagementAddresses() {
		return managementAddresses;
	}
	public String getSourceInterfaceName() {
		return sourceInterfaceName;
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setManagementAddresses(List<InetAddress> managementAddresses) {
		this.managementAddresses = Collections.unmodifiableList(managementAddresses);
	}
	public void setSourceInterfaceName(String sourceInterfaceName) {
		this.sourceInterfaceName = sourceInterfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	
}