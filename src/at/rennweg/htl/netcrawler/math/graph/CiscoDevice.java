package at.rennweg.htl.netcrawler.math.graph;

import java.net.InetAddress;


public abstract class CiscoDevice extends NetworkDevice {
	
	public CiscoDevice(String name, InetAddress managementAddress) {
		super(name, managementAddress);
	}
	
}