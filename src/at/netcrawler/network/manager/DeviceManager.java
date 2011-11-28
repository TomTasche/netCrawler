package at.netcrawler.network.manager;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import at.andiwand.library.network.ip.IPAddress;
import at.netcrawler.network.Capability;
import at.netcrawler.network.connection.DeviceConnection;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.model.NetworkModelExtension;


public abstract class DeviceManager<C extends DeviceConnection> {
	
	private final NetworkDevice device;
	protected final C connection;
	
	private final Map<NetworkDeviceExtension, DeviceExtensionManager<C>> extensionManagerMap = new LinkedHashMap<NetworkDeviceExtension, DeviceExtensionManager<C>>();
	
	public DeviceManager(NetworkDevice device, C connection) {
		this.device = device;
		this.connection = connection;
	}
	
	public final NetworkDevice getDevice() {
		return device;
	}
	
	public final C getConnection() {
		return connection;
	}
	
	public final boolean hasExtensionManager(
			Class<? extends NetworkDeviceExtension> extensionClass) {
		try {
			NetworkDeviceExtension extension = extensionClass.newInstance();
			
			return hasExtensionManager(extension);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public final boolean hasExtensionManager(NetworkDeviceExtension extension) {
		return extensionManagerMap.containsKey(extension);
	}
	
	public final DeviceExtensionManager<C> getExtensionManager(
			Class<? extends NetworkDeviceExtension> extensionClass) {
		try {
			NetworkDeviceExtension extension = extensionClass.newInstance();
			
			return getExtensionManager(extension);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public final DeviceExtensionManager<C> getExtensionManager(
			NetworkDeviceExtension extension) {
		return extensionManagerMap.get(extension);
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
	
	public final boolean addExtensionManager(NetworkDeviceExtension extension,
			DeviceExtensionManager<C> extensionManager) {
		DeviceManager<C> deviceManager = extensionManager.getDeviceManager();
		
		if (deviceManager == this) {
			return false;
		} else if (deviceManager != null) {
			throw new IllegalArgumentException(
					"The extension manager is already in use!");
		}
		
		for (NetworkModelExtension requiredExtension : extension
				.getRequiredExtensions()) {
			if (!extensionManagerMap.containsKey(requiredExtension)) {
				throw new IllegalArgumentException(
						"Does not contain required extension managers!");
			}
		}
		
		extensionManager.setDeviceManager(this);
		extensionManagerMap.put(
				extension, extensionManager);
		
		return true;
	}
	
	public final boolean removeExtensionManager(NetworkDeviceExtension extension) {
		DeviceExtensionManager<C> extensionManager = extensionManagerMap
				.get(extension);
		DeviceManager<C> deviceManager = extensionManager.getDeviceManager();
		
		if (deviceManager != this) return false;
		
		extensionManagerMap.remove(extension);
		extensionManager.setDeviceManager(null);
		
		return true;
	}
	
	public final void readDevice() throws IOException {
		device.setValue(
				NetworkDevice.IDENTICATION, getIdentication());
		device.setValue(
				NetworkDevice.HOSTNAME, getHostname());
		device.setValue(
				NetworkDevice.SYSTEM, getSystem());
		device.setValue(
				NetworkDevice.UPTIME, getUptime());
		device.setValue(
				NetworkDevice.CAPABILITIES, getCapabilities());
		device.setValue(
				NetworkDevice.INTERFACES, getInterfaces());
		device.setValue(
				NetworkDevice.MANAGEMENT_ADDRESSES, getManagementAddresses());
		// TODO: use generic information
		
		for (Map.Entry<NetworkDeviceExtension, DeviceExtensionManager<C>> entry : extensionManagerMap
				.entrySet()) {
			NetworkDeviceExtension deviceExtension = entry.getKey();
			DeviceExtensionManager<C> extensionManager = entry.getValue();
			
			if (extensionManager.hasExtension()) {
				device.addExtension(deviceExtension);
				extensionManager.readDeviceExtension();
			}
		}
	}
	
	public final void fetchDevice() throws IOException {
		device.clear();
		
		readDevice();
	}
	
}