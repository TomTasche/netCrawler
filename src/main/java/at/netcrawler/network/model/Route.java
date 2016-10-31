package at.netcrawler.network.model;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.network.ip.SubnetMask;


// TODO: implement ultimate route
public class Route {
	
	private IPv4Address network;
	private SubnetMask subnetMask;
	private IPv4Address nextHop;
	
	@Override
	public String toString() {
		return "network: " + network.toString() + subnetMask.toString()
				+ "; next hop: " + nextHop;
	}
	
	public IPv4Address getNetwork() {
		return network;
	}
	
	public SubnetMask getSubnetMask() {
		return subnetMask;
	}
	
	public IPv4Address getNextHop() {
		return nextHop;
	}
	
	public void setNetwork(IPv4Address network) {
		this.network = network;
	}
	
	public void setSubnetMask(SubnetMask subnetMask) {
		this.subnetMask = subnetMask;
	}
	
	public void setNextHop(IPv4Address nextHop) {
		this.nextHop = nextHop;
	}
	
}