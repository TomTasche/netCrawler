package at.netcrawler.network.manager;

import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;


public abstract class GenericDeviceExtensionManager<M extends DeviceManager>
		extends DeviceExtensionManager {
	
	protected NetworkDevice device;
	protected M deviceManager;
	
	public GenericDeviceExtensionManager(
			Class<? extends NetworkDeviceExtension> extensionClass) {
		super(extensionClass);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void setDeviceManager(DeviceManager deviceManager) {
		super.setDeviceManager(deviceManager);
		this.deviceManager = (M) deviceManager;
	}
	
}