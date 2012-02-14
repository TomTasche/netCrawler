package at.netcrawler.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import at.andiwand.library.network.ip.IPv4Address;


public class CDPNeighbors implements Iterable<CDPNeighbors.Neighbor> {
	
	public static class Neighbor {
		private String name;
		private String localInterface;
		private int holdTime;
		private Set<Capability> capabilities;
		private String platform;
		private String remoteInterface;
		private Set<IPv4Address> managementAddresses;
		
		public Neighbor(String name, String localInterface, int holdTime,
				Set<Capability> capabilities, String platform,
				String remoteInterface, Set<IPv4Address> managementAddresses) {
			this.name = name;
			this.localInterface = localInterface;
			this.holdTime = holdTime;
			this.capabilities = Collections
					.unmodifiableSet(new HashSet<Capability>(capabilities));
			this.platform = platform;
			this.remoteInterface = remoteInterface;
			this.managementAddresses = Collections
					.unmodifiableSet(new HashSet<IPv4Address>(
							managementAddresses));
		}
		
		@Override
		public String toString() {
			return "name: " + name + "; local interface: " + localInterface
					+ "; holdtime: " + holdTime + "; capabilities: "
					+ capabilities + "; platform: " + platform
					+ "; remote interface: " + remoteInterface
					+ "; management addresses: " + managementAddresses;
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
	}
	
	private final List<Neighbor> table = new LinkedList<Neighbor>();
	
	@Override
	public String toString() {
		return table.toString();
	}
	
	public List<Neighbor> getCDPNeighbors() {
		return new ArrayList<Neighbor>(table);
	}
	
	public void addNeighbor(Neighbor neighbor) {
		table.add(neighbor);
	}
	
	public void removeNeighbor(Neighbor neighbor) {
		table.remove(neighbor);
	}
	
	@Override
	public Iterator<Neighbor> iterator() {
		return table.iterator();
	}
	
}