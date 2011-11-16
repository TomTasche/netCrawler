package at.netcrawler.network.controller;

import java.io.IOException;

import at.netcrawler.network.model.NetworkDevice;


public abstract class DeviceExtensionSetManager {
	
	protected final NetworkDevice device;
	protected final DeviceManager deviceManager;
	
	
	
	public DeviceExtensionSetManager(DeviceManager deviceManager) {
		this.device = deviceManager.getDevice();
		this.deviceManager = deviceManager;
	}
	
	
	public NetworkDevice getNetworkDevice() {
		return deviceManager.getDevice();
	}
	public DeviceManager getDeviceManager() {
		return deviceManager;
	}
	
	public abstract void fetchDeviceExtensionSet() throws IOException;
	
}