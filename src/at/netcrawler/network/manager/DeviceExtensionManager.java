package at.netcrawler.network.manager;

import java.io.IOException;

import at.netcrawler.network.connection.DeviceConnection;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;


public abstract class DeviceExtensionManager<C extends DeviceConnection> {
	
	private final Class<? extends NetworkDeviceExtension> extensionClass;
	
	private NetworkDevice device;
	private DeviceManager<C> deviceManager;
	protected C connection;
	
	public DeviceExtensionManager(
			Class<? extends NetworkDeviceExtension> extensionClass) {
		this.extensionClass = extensionClass;
	}
	
	public Class<? extends NetworkDeviceExtension> getExtensionClass() {
		return extensionClass;
	}
	
	public final NetworkDevice getDevice() {
		return device;
	}
	
	public final NetworkDevice getNetworkDevice() {
		return deviceManager.getDevice();
	}
	
	public final DeviceManager<C> getDeviceManager() {
		return deviceManager;
	}
	
	public final C getConnection() {
		return connection;
	}
	
	final void setDeviceManager(DeviceManager<C> deviceManager) {
		if (deviceManager == null) {
			this.device = null;
			this.deviceManager = null;
			this.connection = null;
			
			return;
		}
		
		this.device = deviceManager.getDevice();
		this.deviceManager = deviceManager;
		this.connection = deviceManager.connection;
	}
	
	public abstract boolean hasExtension() throws IOException;
	
	public abstract void readDeviceExtension() throws IOException;
	
	public final void fetchDeviceExtension() throws IOException {
		device.clearExtension(extensionClass);
		
		readDeviceExtension();
	}
	
}