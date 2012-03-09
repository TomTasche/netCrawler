package at.netcrawler.ui.graphical;

import java.util.Set;

import at.netcrawler.network.Capability;
import at.netcrawler.network.model.NetworkDevice;


public class NetworkDeviceHelper {
	
	private static final String NEW_LINE = System.getProperty("line.separator");
	
	public static String getHostname(NetworkDevice device) {
		return (String) device.getValue(NetworkDevice.HOSTNAME);
	}
	
	public static String getMajorCapability(NetworkDevice device) {
		return device.getValue(
				NetworkDevice.MAJOR_CAPABILITY).toString();
	}
	
	public static String getUptime(NetworkDevice device) {
		Long uptime = (Long) device.getValue(NetworkDevice.UPTIME);
		if (uptime == null) return "";
		
		return (uptime / 1000) + "s";
	}
	
	public static String getSystem(NetworkDevice device) {
		return (String) device.getValue(NetworkDevice.SYSTEM);
	}
	
	@SuppressWarnings("unchecked")
	public static String getManagementAddresses(NetworkDevice device) {
		Set<Object> addresses = (Set<Object>) device
				.getValue(NetworkDevice.MANAGEMENT_ADDRESSES);
		if (addresses == null) return "";
		
		String s = "";
		for (Object address : addresses) {
			s += address.toString() + NEW_LINE;
		}
		s = s.trim();
		
		return s;
	}
	
	@SuppressWarnings("unchecked")
	public static String concatCapabilities(NetworkDevice device) {
		String caps = "";
		
		Set<Capability> capabilities = (Set<Capability>) device
				.getValue(NetworkDevice.CAPABILITIES);
		if (capabilities == null) return "";
		
		for (Capability capability : capabilities) {
			caps += capability.name().substring(
					0, 1);
		}
		
		return caps;
	}
}
