package at.netcrawler.network.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.network.ip.IPv4Address;


public class CDPNeighbor {
	
	private String name;
	private String localInterface;
	private int holdTime;
	private Set<Capability> capabilities;
	private String platform;
	private String remoteInterface;
	private Set<IPv4Address> managementAddresses;
	
	@Override
	public String toString() {
		return "name: " + name + "; local interface: " + localInterface
				+ "; holdtime: " + holdTime + "; capabilities: " + capabilities
				+ "; platform: " + platform + "; remote interface: "
				+ remoteInterface + "; management addresses: "
				+ managementAddresses;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLocalInterface() {
		return localInterface;
	}
	
	public int getHoldTime() {
		return holdTime;
	}
	
	public Set<Capability> getCapabilities() {
		return capabilities;
	}
	
	public String getPlatform() {
		return platform;
	}
	
	public String getRemoteInterface() {
		return remoteInterface;
	}
	
	public Set<IPv4Address> getManagementAddresses() {
		return managementAddresses;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLocalInterface(String localInterface) {
		this.localInterface = localInterface;
	}
	
	public void setHoldTime(int holdTime) {
		this.holdTime = holdTime;
	}
	
	public void setCapabilities(Set<Capability> capabilities) {
		this.capabilities = Collections
				.unmodifiableSet(new HashSet<Capability>(capabilities));
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	public void setRemoteInterface(String remoteInterface) {
		this.remoteInterface = remoteInterface;
	}
	
	public void setManagementAddresses(Set<IPv4Address> managementAddresses) {
		this.managementAddresses = Collections
				.unmodifiableSet(new HashSet<IPv4Address>(managementAddresses));
	}
	
}