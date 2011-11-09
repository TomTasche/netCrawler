package at.netcrawler.network.controller;

import java.io.IOException;

import at.netcrawler.network.model.NetworkDevice;


public abstract class NetworkDeviceExtensionSetManager {
	
	protected final NetworkDevice device;
	protected final NetworkDeviceManager deviceManager;
	
	
	
	public NetworkDeviceExtensionSetManager(NetworkDeviceManager deviceManager) {
		this.device = deviceManager.getDevice();
		this.deviceManager = deviceManager;
	}
	
	
	public NetworkDevice getNetworkDevice() {
		return deviceManager.getDevice();
	}
	public NetworkDeviceManager getDeviceManager() {
		return deviceManager;
	}
	
	public abstract void fetchDeviceExtensionSet() throws IOException;
	
}