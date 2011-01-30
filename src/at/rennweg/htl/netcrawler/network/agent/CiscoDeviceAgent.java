package at.rennweg.htl.netcrawler.network.agent;

import at.rennweg.htl.netcrawler.network.graph.CiscoDevice;


public abstract class CiscoDeviceAgent extends NetworkDeviceAgent {
	
	public abstract String fetchSeriesNumber();
	
	public abstract String fetchProcessorBoardId();
	
	
	public CiscoDevice fetchAll() {
		return new CiscoDevice(this);
	}
	
}