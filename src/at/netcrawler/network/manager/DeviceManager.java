package at.netcrawler.network.manager;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.network.ip.IPAddress;
import at.netcrawler.network.Capability;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;
import at.netcrawler.network.model.NetworkInterface;


public abstract class DeviceManager {
	
	private final NetworkDevice device;
	
	private final Set<DeviceExtensionManager> extensionManagers = new HashSet<DeviceExtensionManager>();
	
	public DeviceManager(NetworkDevice device) {
		this.device = device;
	}
	
	public final NetworkDevice getDevice() {
		return device;
	}
	
	public final boolean hasExtensionManager(NetworkDeviceExtension extension) {
		for (DeviceExtensionManager extensionManager : extensionManagers) {
			if (extensionManager.equals(extension)) return true;
		}
		
		return false;
	}
	
	public final boolean hasExtensionManager(
			Class<? extends NetworkDeviceExtension> extensionClass) {
		for (DeviceExtensionManager extensionManager : extensionManagers) {
			if (extensionManager.getExtensionClass().equals(extensionClass)) return true;
		}
		
		return false;
	}
	
	public final boolean hasExtensionManager(
			DeviceExtensionManager extensionManager) {
		return extensionManagers.contains(extensionManager);
	}
	
	public abstract String getIdentication() throws IOException;
	
	public abstract String getHostname() throws IOException;
	
	public abstract String getSystem() throws IOException;
	
	public abstract long getUptime() throws IOException;
	
	public abstract Set<Capability> getCapabilities() throws IOException;
	
	public abstract Set<NetworkInterface> getInterfaces() throws IOException;
	
	public abstract Set<IPAddress> getManagementAddresses() throws IOException;
	
	// TODO: add generic methods
	
	public abstract boolean setHostname(String hostname) throws IOException;
	
	// TODO: add generic methods
	
	public final boolean addExtensionManager(
			DeviceExtensionManager extensionManager) {
		DeviceManager deviceManager = extensionManager.getDeviceManager();
		NetworkDeviceExtension deviceExtension = extensionManager.getExtension();
		
		if (deviceManager == this) {
			return false;
		} else if (deviceManager != null) {
			throw new IllegalArgumentException(
					"The extension manager is already in use!");
		}
		
		for (NetworkDeviceExtension requiredExtension : deviceExtension.getRequiredExtensions()) {
			if (!hasExtensionManager(requiredExtension)) {
				throw new IllegalArgumentException(
						"Does not contain required extension managers!");
			}
		}
		
		extensionManager.setDeviceManager(this);
		extensionManagers.add(extensionManager);
		
		return true;
	}
	
	public final boolean removeExtensionManager(
			DeviceExtensionManager extensionManager) {
		DeviceManager deviceManager = extensionManager.getDeviceManager();
		
		if (deviceManager != this) return false;
		
		extensionManagers.remove(extensionManager);
		extensionManager.setDeviceManager(null);
		
		return true;
	}
	
	public void readDevice() throws IOException {
		device.setValue(NetworkDevice.IDENTICATION, getIdentication());
		device.setValue(NetworkDevice.HOSTNAME, getHostname());
		device.setValue(NetworkDevice.SYSTEM, getSystem());
		device.setValue(NetworkDevice.UPTIME, getUptime());
		device.setValue(NetworkDevice.CAPABILITIES, getCapabilities());
		device.setValue(NetworkDevice.INTERFACES, getInterfaces());
		device.setValue(NetworkDevice.MANAGEMENT_ADDRESSES,
				getManagementAddresses());
		// TODO: use generic information
		
		for (DeviceExtensionManager extensionManager : extensionManagers) {
			if (extensionManager.hasExtension()) {
				device.addExtension(extensionManager.getExtension());
				extensionManager.readDeviceExtension();
			}
		}
	}
	
	public void fetchDevice() throws IOException {
		device.clear();
		
		readDevice();
	}
	
}