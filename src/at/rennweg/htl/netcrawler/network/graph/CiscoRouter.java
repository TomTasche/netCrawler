package at.rennweg.htl.netcrawler.network.graph;

import java.net.InetAddress;


public class CiscoRouter extends CiscoDevice {
	
	public CiscoRouter() {
		super();
	}
	public CiscoRouter(String name, InetAddress managementAddress) {
		super(name, managementAddress);
	}
	
}