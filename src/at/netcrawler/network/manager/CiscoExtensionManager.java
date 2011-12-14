package at.netcrawler.network.manager;

import java.io.IOException;

import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.extension.CiscoExtension;


public abstract class CiscoExtensionManager extends DeviceExtensionManager {
	
	private static final Class<CiscoExtension> EXTENSION_CLASS = CiscoExtension.class;
	
	public CiscoExtensionManager() {
		super(EXTENSION_CLASS);
	}
	
	public abstract String getModelNumber() throws IOException;
	
	public abstract String getSystemSerialNumber() throws IOException;
	
	public abstract String getProcessorString() throws IOException;
	
	@Override
	public boolean hasExtension() throws IOException {
		// TODO: implement
		return true;
	}
	
	@Override
	public void readDeviceExtension() throws IOException {
		NetworkDevice device = getDevice();
		
		device.setValue(CiscoExtension.MODEL_NUMBER, getModelNumber());
		device.setValue(CiscoExtension.SYSTEM_SERIAL_NUMBER,
				getSystemSerialNumber());
		device.setValue(CiscoExtension.PROCESSOR_STRING, getProcessorString());
	}
	
}