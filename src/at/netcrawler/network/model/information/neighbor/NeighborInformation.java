package at.netcrawler.network.model.information.neighbor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.network.ip.IPv4Address;


public abstract class NeighborInformation {
	
	private final Set<IPv4Address> managementAddresses;
	
	public NeighborInformation(Set<IPv4Address> managementAddresses) {
		this.managementAddresses = Collections
				.unmodifiableSet(new HashSet<IPv4Address>(managementAddresses));
	}
	
	public Set<IPv4Address> getManagementAddresses() {
		return managementAddresses;
	}
	
}