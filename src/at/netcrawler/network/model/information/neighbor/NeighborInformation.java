package at.netcrawler.network.model.information.neighbor;

import java.util.Set;

import at.andiwand.library.network.ip.IPv4Address;


public interface NeighborInformation {
	
	public Set<IPv4Address> getManagementAddresses();
	
}