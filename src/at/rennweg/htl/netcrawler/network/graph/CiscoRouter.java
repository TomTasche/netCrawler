package at.rennweg.htl.netcrawler.network.graph;

import java.net.InetAddress;
import java.util.Set;


public class CiscoRouter extends CiscoDevice {
	
	protected String processorBoardId;
	
	
	
	public CiscoRouter() {
		super();
	}
	public CiscoRouter(String name) {
		super(name);
	}
	public CiscoRouter(String name, Set<InetAddress> managementAddresses) {
		super(name, managementAddresses);
	}
	public CiscoRouter(Set<NetworkInterface> interfaces, String hostname, Set<InetAddress> managementAddresses, String seriesNumber, String processorBoardId) {
		super(interfaces, hostname, managementAddresses, seriesNumber);
		
		this.processorBoardId = processorBoardId;
	}
	
	
	
	//TODO: maybe - fix both
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		
		if (!(obj instanceof CiscoRouter)) return false;
		CiscoRouter router = (CiscoRouter) obj;
		
		if (processorBoardId == null) return false;
		return processorBoardId.equals(router.processorBoardId)
			&& hostname.equals(router.hostname);
	}
	@Override
	public int hashCode() {
		if (processorBoardId == null) return 0;
		
		return processorBoardId.hashCode() ^ hostname.hashCode();
	}
	
	
	public String getProcessorBoardId() {
		return processorBoardId;
	}
	
	public void setProcessorBoardId(String processorBoardId) {
		this.processorBoardId = processorBoardId;
	}
	
}