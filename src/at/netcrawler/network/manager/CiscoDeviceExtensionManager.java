package at.netcrawler.network.manager;

import java.io.IOException;

import at.netcrawler.network.CDPNeighbors;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.extension.CiscoDeviceExtension;


public abstract class CiscoDeviceExtensionManager extends DeviceExtensionManager {
	
	private static final Class<CiscoDeviceExtension> EXTENSION_CLASS = CiscoDeviceExtension.class;
	
	public CiscoDeviceExtensionManager() {
		super(EXTENSION_CLASS);
	}
	
	@Override
	public final Object getValue(String key) throws IOException {
		if (key.equals(CiscoDeviceExtension.MODEL_NUMBER)) {
			return getModelNumber();
		} else if (key.equals(CiscoDeviceExtension.SYSTEM_SERIAL_NUMBER)) {
			return getSystemSerialNumber();
		} else if (key.equals(CiscoDeviceExtension.PROCESSOR_STRING)) {
			return getProcessorString();
		} else if (key.equals(CiscoDeviceExtension.CDP_NEIGHBORS)) {
			return getCDPNeighbors();
		}
		
		throw new IllegalArgumentException("Unsupported key!");
	}
	
	public abstract String getModelNumber() throws IOException;
	
	public abstract String getSystemSerialNumber() throws IOException;
	
	public abstract String getProcessorString() throws IOException;
	
	public abstract CDPNeighbors getCDPNeighbors() throws IOException;
	
	@Override
	public boolean setValue(String key, Object value) throws IOException {
		throw new IllegalArgumentException("Unsupported key!");
	}
	
	// TODO: improve
	@Override
	public boolean hasExtension() throws IOException {
		String system = (String) device.getValue(NetworkDevice.SYSTEM);
		if (system == null) return false;
		return system.toLowerCase().contains("cisco");
	}
	
}