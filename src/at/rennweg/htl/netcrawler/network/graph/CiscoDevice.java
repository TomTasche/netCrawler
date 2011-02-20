package at.rennweg.htl.netcrawler.network.graph;

import java.net.InetAddress;
import java.util.Set;

import at.rennweg.htl.netcrawler.network.agent.CiscoDeviceAgent;


public class CiscoDevice extends NetworkDevice {
	
	protected String seriesNumber;
	
	protected String processorBoardId;
	
	
	public CiscoDevice() {
		super();
	}
	public CiscoDevice(String hostname, Set<InetAddress> managementAddresses) {
		super(hostname, managementAddresses);
	}
	public CiscoDevice(Set<NetworkInterface> interfaces, String hostname, Set<InetAddress> managementAddresses, String seriesNumber, String processorBoardId) {
		super(interfaces, hostname, managementAddresses);
		
		this.seriesNumber = seriesNumber;
		
		this.processorBoardId = processorBoardId;
	}
	public CiscoDevice(CiscoDeviceAgent deviceAgent) {
		super(deviceAgent);
		
		seriesNumber = deviceAgent.fetchSeriesNumber();
		
		processorBoardId = deviceAgent.fetchProcessorBoardId();
	}
	
	
	// TODO maybe: fix both
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		
		if (!(obj instanceof CiscoDevice)) return false;
		CiscoDevice device = (CiscoDevice) obj;
		
		if (processorBoardId == null) return false;
		return processorBoardId.equals(device.processorBoardId)
			&& hostname.equals(device.hostname);
	}
	@Override
	public int hashCode() {
		if (processorBoardId == null) return 0;
		
		return processorBoardId.hashCode() ^ hostname.hashCode();
	}
	
	
	public String getSeriesNumber() {
		return seriesNumber;
	}
	public String getProcessorBoardId() {
		return processorBoardId;
	}
	
	public void setSeriesNumber(String seriesNumber) {
		this.seriesNumber = seriesNumber;
	}
	public void setProcessorBoardId(String processorBoardId) {
		this.processorBoardId = processorBoardId;
	}
	
}