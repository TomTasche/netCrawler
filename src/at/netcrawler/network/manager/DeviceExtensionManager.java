package at.netcrawler.network.manager;

import java.io.IOException;

import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;


// TODO: add requirements?
public abstract class DeviceExtensionManager {
	
	private final Class<? extends NetworkDeviceExtension> extensionClass;
	
	protected NetworkDevice device;
	protected DeviceManager deviceManager;
	
	public DeviceExtensionManager(
			Class<? extends NetworkDeviceExtension> extensionClass) {
		this.extensionClass = extensionClass;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof DeviceExtensionManager)) return false;
		DeviceExtensionManager manager = (DeviceExtensionManager) obj;
		
		return extensionClass.equals(manager.extensionClass);
	}
	
	public int hashCode() {
		return extensionClass.hashCode();
	}
	
	public final Class<? extends NetworkDeviceExtension> getExtensionClass() {
		return extensionClass;
	}
	
	public abstract Object getValue(String key) throws IOException;
	
	public final NetworkDeviceExtension getExtension() {
		try {
			return extensionClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public final NetworkDevice getDevice() {
		return device;
	}
	
	public final NetworkDevice getNetworkDevice() {
		return deviceManager.getDevice();
	}
	
	public final DeviceManager getDeviceManager() {
		return deviceManager;
	}
	
	public abstract boolean setValue(String key, Object value)
			throws IOException;
	
	protected void setDeviceManager(DeviceManager deviceManager) {
		if (deviceManager == null) {
			this.device = null;
			this.deviceManager = null;
			
			return;
		}
		
		this.device = deviceManager.getDevice();
		this.deviceManager = deviceManager;
	}
	
	public abstract boolean hasExtension() throws IOException;
	
}