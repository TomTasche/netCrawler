package at.rennweg.htl.netcrawler.network.graph;

import java.net.InetAddress;
import java.util.Set;


public class CiscoSwitch extends CiscoDevice {
	
	public CiscoSwitch() {
		super();
	}
	public CiscoSwitch(String name, Set<InetAddress> managementAddresses) {
		super(name, managementAddresses);
	}
	
}