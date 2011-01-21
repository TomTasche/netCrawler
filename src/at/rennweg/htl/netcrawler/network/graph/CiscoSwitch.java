package at.rennweg.htl.netcrawler.network.graph;

import java.net.InetAddress;


public class CiscoSwitch extends CiscoDevice {
	
	public CiscoSwitch() {
		super();
	}
	public CiscoSwitch(String name, InetAddress managementAddress) {
		super(name, managementAddress);
	}
	
}