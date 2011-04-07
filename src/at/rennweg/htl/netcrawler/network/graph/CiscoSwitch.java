package at.rennweg.htl.netcrawler.network.graph;

import java.net.InetAddress;
import java.util.Set;


public class CiscoSwitch extends CiscoDevice {
	
	private String motherboardSerialNumber;
	
	
	
	public CiscoSwitch() {
		super();
	}
	public CiscoSwitch(String name) {
		super(name);
	}
	public CiscoSwitch(String name, Set<InetAddress> managementAddresses) {
		super(name, managementAddresses);
	}
	public CiscoSwitch(Set<NetworkInterface> interfaces, String hostname, Set<InetAddress> managementAddresses, String seriesNumber, String motherboardSerialNumber) {
		super(interfaces, hostname, managementAddresses, seriesNumber);
		
		this.motherboardSerialNumber = motherboardSerialNumber;
	}
	
	
	
	//TODO: maybe - fix both
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		
		if (!(obj instanceof CiscoSwitch)) return false;
		CiscoSwitch ciscoSwitch = (CiscoSwitch) obj;
		
		if (motherboardSerialNumber == null) return false;
		return motherboardSerialNumber.equals(ciscoSwitch.motherboardSerialNumber)
			&& hostname.equals(ciscoSwitch.hostname);
	}
	@Override
	public int hashCode() {
		if (motherboardSerialNumber == null) return 0;
		
		return motherboardSerialNumber.hashCode() ^ hostname.hashCode();
	}
	
	
	public String getMotherboardSerialNumber() {
		return motherboardSerialNumber;
	}
	
	public void setMotherboardSerialNumber(String motherboardSerialNumber) {
		this.motherboardSerialNumber = motherboardSerialNumber;
	}
	
}