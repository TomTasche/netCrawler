package at.netcrawler.network.model.information.neighbor;

import java.util.Set;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.network.model.CDPNeighbor;
import at.netcrawler.network.model.Capability;
import at.netcrawler.network.model.DeviceSystem;


public class CDPNeighborInformation implements DetailedNeighborInformation {
	
	private final CDPNeighbor cdpNeighbor;
	
	public CDPNeighborInformation(CDPNeighbor cdpNeighbor) {
		this.cdpNeighbor = cdpNeighbor.clone();
	}
	
	public String getHostname() {
		return cdpNeighbor.getHostname();
	}
	
	public DeviceSystem getSystem() {
		return DeviceSystem.CISCO;
	}
	
	public String getSystemDescription() {
		return cdpNeighbor.getSystemDescription();
	}
	
	public Set<Capability> getCapabilities() {
		return cdpNeighbor.getCapabilities();
	}
	
	public String getLocalInterface() {
		return cdpNeighbor.getLocalInterface();
	}
	
	public String getRemoteInterface() {
		return cdpNeighbor.getRemoteInterface();
	}
	
	public Set<IPv4Address> getManagementAddresses() {
		return cdpNeighbor.getManagementAddresses();
	}
	
}