package at.netcrawler.network;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.network.mac.MACAddress;


public class ARPEntry {
	
	private MACAddress macAddress;
	private IPv4Address ipAddress;
	
	public String toString() {
		return ipAddress.toDottedString() + " -> " + macAddress;
	}
	
	public MACAddress getMACAddress() {
		return macAddress;
	}
	
	public IPv4Address getIPv4Address() {
		return ipAddress;
	}
	
	public void setMacAddress(MACAddress address) {
		this.macAddress = address;
	}
	
	public void setIPv4Address(IPv4Address address) {
		this.ipAddress = address;
	}
	
}