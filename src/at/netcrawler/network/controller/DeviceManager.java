package at.netcrawler.network.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.andiwand.library.util.GenericsUtil;
import at.netcrawler.network.Capability;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtensionSet;
import at.netcrawler.network.model.NetworkInterface;


public abstract class DeviceManager {
	
	private final NetworkDevice device;
	
	private final Map<NetworkDeviceExtensionSet, DeviceExtensionSetManager> extensionSetManagerMap = new HashMap<NetworkDeviceExtensionSet, DeviceExtensionSetManager>();
	
	
	
	public DeviceManager(NetworkDevice device) {
		this.device = device;
	}
	
	
	
	public NetworkDevice getDevice() {
		return device;
	}
	public boolean hasExtensionSetManager(NetworkDeviceExtensionSet extensionSet) {
		return extensionSetManagerMap.containsKey(extensionSet);
	}

	public DeviceExtensionSetManager getExtensionSetManager(
			NetworkDeviceExtensionSet extensionSet) {
		return extensionSetManagerMap.get(extensionSet);
	}
	public abstract String getIdentication() throws IOException;
	public abstract String getHostname() throws IOException;
	public abstract String getSystem() throws IOException;
	public abstract Set<Capability> getCapabilities() throws IOException;
	public abstract Set<NetworkInterface> getInterfaces() throws IOException;
	// TODO: add generic methods
	
	public abstract void setHostname(String hostname) throws IOException;
	// TODO: add generic methods
	
	
	public void addExtensionSetManager(NetworkDeviceExtensionSet extensionSet,
			DeviceExtensionSetManager extensionManager) {
		extensionSetManagerMap.put(extensionSet, extensionManager);
	}
	
	public void removeExtensionSetManager(NetworkDeviceExtensionSet extensionSet) {
		extensionSetManagerMap.remove(extensionSet);
	}
	
	
	public void fetchDevice() throws IOException {
		device.clear();
		
		device.setValue(NetworkDevice.IDENTICATION, getIdentication());
		device.setValue(NetworkDevice.HOSTNAME, getHostname());
		device.setValue(NetworkDevice.SYSTEM, getSystem());
		device.setValue(NetworkDevice.CAPABILITIES, getCapabilities());
		device.setValue(NetworkDevice.INTERFACES, getInterfaces());
		// TODO: use generic information
		
		Set<Capability> capabilities = GenericsUtil.castObject(device
				.getValue(NetworkDevice.CAPABILITIES));
		
		if (capabilities.contains(Capability.ROUTER)
				&& hasExtensionSetManager(NetworkDeviceExtensionSet.ROUTER)) {
			getExtensionSetManager(NetworkDeviceExtensionSet.ROUTER)
					.fetchDeviceExtensionSet();
		}
	}
	
}