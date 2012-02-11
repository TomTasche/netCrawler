package at.netcrawler.network.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import at.andiwand.library.network.ip.IPAddress;
import at.netcrawler.network.Capability;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;
import at.netcrawler.network.model.NetworkInterface;


public abstract class DeviceManager {
	
	private final NetworkDevice device;
	
	private final Set<DeviceExtensionManager> extensionManagers = new LinkedHashSet<DeviceExtensionManager>();
	
	private double progress;
	// TODO: or polling?
	private List<ManagerProgressListener> progressListeners = new ArrayList<ManagerProgressListener>();
	
	public DeviceManager(NetworkDevice device) {
		this.device = device;
	}
	
	public final NetworkDevice getDevice() {
		return device;
	}
	
	public final double getProgress() {
		return progress;
	}
	
	public final Object getValue(String key) throws IOException {
		if (key.equals(NetworkDevice.IDENTICATION)) {
			return getIdentication();
		} else if (key.equals(NetworkDevice.HOSTNAME)) {
			return getHostname();
		} else if (key.equals(NetworkDevice.SYSTEM)) {
			return getSystem();
		} else if (key.equals(NetworkDevice.UPTIME)) {
			return getUptime();
		} else if (key.equals(NetworkDevice.CAPABILITIES)) {
			return getCapabilities();
		} else if (key.equals(NetworkDevice.MAJOR_CAPABILITY)) {
			return getMajorCapability();
		} else if (key.equals(NetworkDevice.INTERFACES)) {
			return getInterfaces();
		} else if (key.equals(NetworkDevice.MANAGEMENT_ADDRESSES)) {
			return getManagementAddresses();
		}
		
		// TODO: optimize (string->extensionManager map)
		synchronized (extensionManagers) {
			for (DeviceExtensionManager extensionManager : extensionManagers) {
				try {
					return extensionManager.getValue(key);
				} catch (RuntimeException e) {}
			}
		}
		
		throw new IllegalArgumentException("Unsupported key!");
	}
	
	public abstract String getIdentication() throws IOException;
	
	public abstract String getHostname() throws IOException;
	
	public abstract String getSystem() throws IOException;
	
	public abstract long getUptime() throws IOException;
	
	public abstract Set<Capability> getCapabilities() throws IOException;
	
	public abstract Capability getMajorCapability() throws IOException;
	
	public abstract Set<NetworkInterface> getInterfaces() throws IOException;
	
	public abstract Set<IPAddress> getManagementAddresses() throws IOException;
	
	public final boolean setValue(String key, Object value) throws IOException {
		if (key.equals(NetworkDevice.HOSTNAME)) {
			return setHostname((String) value);
		}
		
		// TODO: optimize (string->extensionManager map)
		synchronized (extensionManagers) {
			for (DeviceExtensionManager extensionManager : extensionManagers) {
				try {
					return extensionManager.setValue(key, value);
				} catch (IllegalArgumentException e) {}
			}
		}
		
		throw new IllegalArgumentException("Unsupported key!");
	}
	
	public abstract boolean setHostname(String hostname) throws IOException;
	
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
	
	// TODO: or polling?
	public void addProgressListener(ManagerProgressListener listener) {
		synchronized (progressListeners) {
			progressListeners.add(listener);
		}
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
	
	// TODO: or polling?
	public void removeProgressListener(ManagerProgressListener listener) {
		synchronized (progressListeners) {
			progressListeners.remove(listener);
		}
	}
	
	public abstract Set<IPAddress> discoverNeighbors();
	
	public final void updateDevice() throws IOException {
		progress = 0;
		
		Set<String> keySet = new HashSet<String>();
		keySet.addAll(NetworkDevice.TYPE_MAP.keySet());
		
		synchronized (extensionManagers) {
			for (DeviceExtensionManager extensionManager : extensionManagers) {
				if (!extensionManager.hasExtension()) continue;
				
				NetworkDeviceExtension extension = extensionManager.getExtension();
				Set<String> extensionKexSet = extension.getExtensionTypeMap().keySet();
				keySet.addAll(extensionKexSet);
			}
		}
		
		int keyCount = keySet.size();
		int fetchCount = 0;
		
		for (String key : keySet) {
			Object value = getValue(key);
			device.setValue(key, value);
			
			fetchCount++;
			progress = (double) fetchCount / keyCount;
			// TODO: or polling?
			fireProgress(progress);
		}
	}
	
	public final void fetchDevice() throws IOException {
		device.clear();
		
		updateDevice();
	}
	
	// TODO: or polling?
	private void fireProgress(double progress) {
		synchronized (progressListeners) {
			for (ManagerProgressListener progressListener : progressListeners) {
				progressListener.updateOccured(progress);
			}
		}
	}
	
}