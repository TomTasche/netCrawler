package at.netcrawler.network;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.network.ip.SubnetMask;


public class RoutingTable implements Iterable<RoutingTable.Route> {
	
	public static class Route {
		private IPv4Address network;
		private SubnetMask subnetMask;
		private IPv4Address nextHop;
		
		
		public Route(IPv4Address network, SubnetMask subnetMask, IPv4Address nextHop) {
			this.network = network;
			this.subnetMask = subnetMask;
			this.nextHop = nextHop;
		}
		
		
		@Override
		public String toString() {
			return "network: " + network.toString() + subnetMask.toString()
					+ "next hop: " + nextHop;
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
	}
	
	
	
	
	private List<Route> table = new LinkedList<Route>();
	
	
	
	@Override
	public String toString() {
		return table.toString();
	}
	
	
	public List<Route> getRoutingTable() {
		return Collections.unmodifiableList(table);
	}
	
	
	public void addRoute(Route route) {
		table.add(route);
	}
	
	public void removeRoute(Route route) {
		table.remove(route);
	}
	
	
	@Override
	public Iterator<Route> iterator() {
		return table.iterator();
	}
	
}