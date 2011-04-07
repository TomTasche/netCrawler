package at.rennweg.htl.netcrawler.network.graph;

import java.net.InetAddress;
import java.util.Set;


public class CiscoDevice extends NetworkDevice {
	
	protected String seriesNumber;
	
	
	public CiscoDevice() {
		super();
	}
	public CiscoDevice(String hostname) {
		super(hostname);
	}
	public CiscoDevice(String hostname, Set<InetAddress> managementAddresses) {
		super(hostname, managementAddresses);
	}
	public CiscoDevice(Set<NetworkInterface> interfaces, String hostname, Set<InetAddress> managementAddresses, String seriesNumber) {
		super(interfaces, hostname, managementAddresses);
		
		this.seriesNumber = seriesNumber;
	}
	
	
	public String getSeriesNumber() {
		return seriesNumber;
	}
	
	public void setSeriesNumber(String seriesNumber) {
		this.seriesNumber = seriesNumber;
	}
	
}