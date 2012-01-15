package at.netcrawler.network.manager;

import at.netcrawler.network.model.NetworkDeviceExtension;


public abstract class GenericDeviceExtensionManager<M extends DeviceManager> extends
		DeviceExtensionManager {
	
	protected M deviceManager;
	
	public GenericDeviceExtensionManager(
			Class<? extends NetworkDeviceExtension> extensionClass) {
		super(extensionClass);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected final void setDeviceManager(DeviceManager deviceManager) {
		setDeviceManagerGeneric((M) deviceManager);
	}
	
	protected void setDeviceManagerGeneric(M deviceManager) {
		super.setDeviceManager(deviceManager);
		
		this.deviceManager = deviceManager;
	}
	
}