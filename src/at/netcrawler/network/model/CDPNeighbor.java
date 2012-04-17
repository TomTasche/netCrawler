package at.netcrawler.network.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.network.ip.IPv4Address;


public class CDPNeighbor {
	
	private String hostname;
	private String systemDescription;
	private Set<Capability> capabilities;
	private Set<IPv4Address> managementAddresses;
	private String localInterface;
	private String remoteInterface;
	private int holdTime;
	
	public CDPNeighbor() {}
	
	public CDPNeighbor(CDPNeighbor neighbor) {
		this.hostname = neighbor.hostname;
		this.systemDescription = neighbor.systemDescription;
		this.capabilities = neighbor.capabilities;
		this.managementAddresses = neighbor.managementAddresses;
		this.localInterface = neighbor.localInterface;
		this.remoteInterface = neighbor.remoteInterface;
		this.holdTime = neighbor.holdTime;
	}
	
	@Override
	public String toString() {
		return "hostname: " + hostname + "; local interface: " + localInterface
				+ "; holdtime: " + holdTime + "; capabilities: " + capabilities
				+ "; platform: " + systemDescription + "; remote interface: "
				+ remoteInterface + "; management addresses: "
				+ managementAddresses;
	}
	
	@Override
	public CDPNeighbor clone() {
		return new CDPNeighbor(this);
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public String getSystemDescription() {
		return systemDescription;
	}
	
	public Set<Capability> getCapabilities() {
		return capabilities;
	}
	
	public Set<IPv4Address> getManagementAddresses() {
		return managementAddresses;
	}
	
	public String getLocalInterface() {
		return localInterface;
	}
	
	public String getRemoteInterface() {
		return remoteInterface;
	}
	
	public int getHoldTime() {
		return holdTime;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public void setSystemDescription(String systemDescription) {
		this.systemDescription = systemDescription;
	}
	
	public void setCapabilities(Set<Capability> capabilities) {
		this.capabilities = Collections
				.unmodifiableSet(new HashSet<Capability>(capabilities));
	}
	
	public void setManagementAddresses(Set<IPv4Address> managementAddresses) {
		this.managementAddresses = Collections
				.unmodifiableSet(new HashSet<IPv4Address>(managementAddresses));
	}
	
	public void setLocalInterface(String localInterface) {
		this.localInterface = localInterface;
	}
	
	public void setRemoteInterface(String remoteInterface) {
		this.remoteInterface = remoteInterface;
	}
	
	public void setHoldTime(int holdTime) {
		this.holdTime = holdTime;
	}
	
}