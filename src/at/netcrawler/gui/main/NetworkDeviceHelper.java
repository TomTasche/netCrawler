package at.netcrawler.gui.main;

import java.util.Set;

import at.netcrawler.network.Capability;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkModel;

public class NetworkDeviceHelper {
	
	public static String getHostname(NetworkModel device) {
		return (String) device.getValue(NetworkDevice.HOSTNAME);
	}
	
	public static String concatCapabilities(NetworkModel device) {
		String caps = "";
		
		Set<Capability> capabilities = (Set<Capability>) device.getValue(NetworkDevice.CAPABILITIES);
		for (Capability capability : capabilities) {
			caps += capability.name().substring(0, 1);
		}
		
		return caps;
	}
}
