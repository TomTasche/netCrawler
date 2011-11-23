package at.netcrawler.network.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.andiwand.library.util.GenericsUtil;
import at.netcrawler.network.Capability;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.model.extension.RouterExtension;


public abstract class DeviceManager {
	
	private final NetworkDevice device;
	
	private final Map<NetworkDeviceExtension, DeviceExtensionSetManager> extensionManagerMap = new HashMap<NetworkDeviceExtension, DeviceExtensionSetManager>();
	
	
	
	public DeviceManager(NetworkDevice device) {
		this.device = device;
	}
	
	
	
	public NetworkDevice getDevice() {
		return device;
	}
	public boolean hasExtensionManager(
			Class<? extends NetworkDeviceExtension> extensionClass) {
		try {
			NetworkDeviceExtension extension = extensionClass.newInstance();
			
			return hasExtensionManager(extension);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public boolean hasExtensionManager(NetworkDeviceExtension extension) {
		return extensionManagerMap.containsKey(extension);
	}
	
	public DeviceExtensionSetManager getExtensionManager(
			Class<? extends NetworkDeviceExtension> extensionClass) {
		try {
			NetworkDeviceExtension extension = extensionClass.newInstance();
			
			return getExtensionManager(extension);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public DeviceExtensionSetManager getExtensionManager(
			NetworkDeviceExtension extension) {
		return extensionManagerMap.get(extension);
	}
	public abstract String getIdentication() throws IOException;
	public abstract String getHostname() throws IOException;
	public abstract String getSystem() throws IOException;
	public abstract Set<Capability> getCapabilities() throws IOException;
	public abstract Set<NetworkInterface> getInterfaces() throws IOException;
	// TODO: add generic methods
	
	public abstract void setHostname(String hostname) throws IOException;
	// TODO: add generic methods
	
	
	public void addExtensionManager(NetworkDeviceExtension extension,
			DeviceExtensionSetManager extensionManager) {
		extensionManagerMap.put(extension, extensionManager);
	}
	
	public void removeExtensionManager(NetworkDeviceExtension extension) {
		extensionManagerMap.remove(extension);
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
				&& hasExtensionManager(RouterExtension.class)) {
			getExtensionManager(RouterExtension.class)
					.fetchDeviceExtensionSet();
		}
	}
	
}