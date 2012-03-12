package at.netcrawler.network.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import at.andiwand.library.network.ip.IPAddress;
import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.DeviceSystem;
import at.netcrawler.network.Capability;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;
import at.netcrawler.network.model.NetworkInterface;


// TODO: move the NetworkDevice to the access methods
// TODO: progress?
public abstract class DeviceManager {
	
	protected final NetworkDevice device;
	
	private final Set<DeviceExtensionManager> extensionManagers = new LinkedHashSet<DeviceExtensionManager>();
	
	public DeviceManager(NetworkDevice device) {
		this.device = device;
	}
	
	public final NetworkDevice getDevice() {
		return device;
	}
	
	public final Object getValue(String key) throws IOException {
		Object result = null;
		
		if (key.equals(NetworkDevice.HOSTNAME)) {
			result = getHostname();
		} else if (key.equals(NetworkDevice.SYSTEM)) {
			result = getSystem();
		} else if (key.equals(NetworkDevice.SYSTEM_STRING)) {
			result = getSystemString();
		} else if (key.equals(NetworkDevice.UPTIME)) {
			result = getUptime();
		} else if (key.equals(NetworkDevice.CAPABILITIES)) {
			result = getCapabilities();
		} else if (key.equals(NetworkDevice.MAJOR_CAPABILITY)) {
			result = getMajorCapability();
		} else if (key.equals(NetworkDevice.INTERFACES)) {
			result = getInterfaces();
		} else if (key.equals(NetworkDevice.MANAGEMENT_ADDRESSES)) {
			result = getManagementAddresses();
		} else {
			// TODO: improve (string->extensionManager map)
			synchronized (extensionManagers) {
				for (DeviceExtensionManager extensionManager : extensionManagers) {
					try {
						result = extensionManager.getValue(key);
					} catch (RuntimeException e) {}
				}
			}
			
			if (result == null)
				throw new IllegalArgumentException("Unsupported key " + key);
		}
		
		device.setValue(key, result);
		return result;
	}
	
	protected abstract String getIdentication() throws IOException;
	
	protected abstract String getHostname() throws IOException;
	
	protected abstract DeviceSystem getSystem() throws IOException;
	
	protected abstract String getSystemString() throws IOException;
	
	protected abstract long getUptime() throws IOException;
	
	protected abstract Set<Capability> getCapabilities() throws IOException;
	
	protected abstract Capability getMajorCapability() throws IOException;
	
	protected abstract Set<NetworkInterface> getInterfaces() throws IOException;
	
	protected abstract Set<IPAddress> getManagementAddresses()
			throws IOException;
	
	public final boolean setValue(String key, Object value) throws IOException {
		Boolean result = null;
		
		if (key.equals(NetworkDevice.HOSTNAME)) {
			result = setHostname((String) value);
		} else {
			// TODO: improve (string->extensionManager map)
			synchronized (extensionManagers) {
				for (DeviceExtensionManager extensionManager : extensionManagers) {
					try {
						result = extensionManager.setValue(key, value);
					} catch (IllegalArgumentException e) {}
				}
			}
		}
		
		if (result == null)
			throw new IllegalArgumentException("Unsupported key!");
		if (result) device.setValue(key, value);
		return result;
	}
	
	protected abstract boolean setHostname(String hostname) throws IOException;
	
	public final boolean hasExtensionManager(NetworkDeviceExtension extension) {
		return hasExtensionManager(extension.getClass());
	}
	
	public final boolean hasExtensionManager(
			Class<? extends NetworkDeviceExtension> extensionClass) {
		for (DeviceExtensionManager extensionManager : extensionManagers) {
			if (extensionManager.getExtensionClass().equals(extensionClass))
				return true;
		}
		
		return false;
	}
	
	public final boolean hasExtensionManager(
			DeviceExtensionManager extensionManager) {
		synchronized (extensionManagers) {
			return extensionManagers.contains(extensionManager);
		}
	}
	
	public final boolean addExtensionManager(
			DeviceExtensionManager extensionManager) {
		if (hasExtensionManager(extensionManager)) return false;
		
		DeviceManager deviceManager = extensionManager.getDeviceManager();
		
		if (deviceManager == this) return false;
		if (deviceManager != null)
			throw new IllegalArgumentException(
					"The extension manager is already in use!");
		
		extensionManager.setDeviceManager(this);
		
		synchronized (extensionManagers) {
			extensionManagers.add(extensionManager);
		}
		
		return true;
	}
	
	public final boolean removeExtensionManager(
			DeviceExtensionManager extensionManager) {
		if (!hasExtensionManager(extensionManager)) return false;
		
		DeviceManager deviceManager = extensionManager.getDeviceManager();
		if (deviceManager != this) return false;
		
		synchronized (extensionManagers) {
			extensionManagers.remove(extensionManager);
		}
		
		extensionManager.setDeviceManager(null);
		
		return true;
	}
	
	public abstract Set<IPv4Address> discoverNeighbors();
	
	public final NetworkDevice complete() throws IOException {
		List<String> keys = new ArrayList<String>();
		keys.addAll(NetworkDevice.TYPE_MAP.keySet());
		keys.removeAll(device.getValueMap().keySet());
		
		for (String key : keys) {
			getValue(key);
		}
		
		keys.clear();
		
		synchronized (extensionManagers) {
			for (DeviceExtensionManager extensionManager : extensionManagers) {
				if (!extensionManager.hasExtension()) continue;
				
				NetworkDeviceExtension extension = extensionManager
						.getExtension();
				device.addExtension(extension);
				Set<String> extensionKexSet = extension.getExtensionTypeMap()
						.keySet();
				keys.addAll(extensionKexSet);
			}
		}
		
		keys.removeAll(device.getValueMap().keySet());
		
		for (String key : keys) {
			getValue(key);
		}
		
		return device;
	}
	
	public final NetworkDevice fetch() throws IOException {
		device.clear();
		return complete();
	}
	
}