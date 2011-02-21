package at.rennweg.htl.netcrawler.network.graph;

import java.net.InetAddress;
import java.util.Set;


public class CiscoRouter extends CiscoDevice {
	
	public CiscoRouter() {
		super();
	}
	public CiscoRouter(String name) {
		super(name);
	}
	public CiscoRouter(String name, Set<InetAddress> managementAddresses) {
		super(name, managementAddresses);
	}
	
}