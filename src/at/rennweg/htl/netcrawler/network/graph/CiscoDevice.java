package at.rennweg.htl.netcrawler.network.graph;

import java.net.InetAddress;


public class CiscoDevice extends NetworkDevice {
	
	protected String seriesNumber;
	
	protected String processorBoardId;
	
	
	public CiscoDevice() {
		super();
	}
	public CiscoDevice(String name, InetAddress managementAddress) {
		super(name, managementAddress);
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		
		if (!(obj instanceof CiscoDevice)) return false;
		CiscoDevice device = (CiscoDevice) obj;
		
		if (processorBoardId == null) return false;
		return processorBoardId.equals(device.processorBoardId);
	}
	@Override
	public int hashCode() {
		if (processorBoardId == null) return 0;
		
		return processorBoardId.hashCode();
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