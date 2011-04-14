package at.rennweg.htl.netcrawler.network.graph;

import java.net.InetAddress;
import java.util.Set;


public class CiscoDevice extends NetworkDevice {
	
	private static final long serialVersionUID = 4557760818556976835L;
	
	
	
	
	protected String seriesNumber;
	
	protected String showVersion;
	protected String dirFlash;
	protected String showRunningConfig;
	protected String showIpInterfaceBrief;
	
	
	
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
	
	public String getShowVersion() {
		return showVersion;
	}
	public String getDirFlash() {
		return dirFlash;
	}
	public String getShowRunningConfig() {
		return showRunningConfig;
	}
	public String getShowIpInterfaceBrief() {
		return showIpInterfaceBrief;
	}
	
	
	public void setSeriesNumber(String seriesNumber) {
		this.seriesNumber = seriesNumber;
	}
	
	public void setShowVersion(String showVersion) {
		this.showVersion = showVersion;
	}
	public void setDirFlash(String dirFlash) {
		this.dirFlash = dirFlash;
	}
	public void setShowRunningConfig(String showRunningConfiguration) {
		this.showRunningConfig = showRunningConfiguration;
	}
	public void setShowIpInterfaceBrief(String showIpInterfaceBrief) {
		this.showIpInterfaceBrief = showIpInterfaceBrief;
	}
	
}