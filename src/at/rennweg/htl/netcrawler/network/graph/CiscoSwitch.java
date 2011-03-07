package at.rennweg.htl.netcrawler.network.graph;

import java.net.InetAddress;
import java.util.Set;

import at.rennweg.htl.netcrawler.network.agent.CiscoDeviceAgent;


public class CiscoSwitch extends CiscoDevice {
	
	public CiscoSwitch() {
		super();
	}
	public CiscoSwitch(String name) {
		super(name);
	}
	public CiscoSwitch(String name, Set<InetAddress> managementAddresses) {
		super(name, managementAddresses);
	}
	public CiscoSwitch(CiscoDeviceAgent deviceAgent) {
		super(deviceAgent);
		
		seriesNumber = deviceAgent.fetchSeriesNumber();
		
		processorBoardId = deviceAgent.fetchProcessorBoardId();
	}
	
}