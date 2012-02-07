package at.netcrawler.network.manager;

import java.io.IOException;

import at.netcrawler.network.CDPNeighbors;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.extension.CiscoExtension;


public abstract class CiscoExtensionManager extends DeviceExtensionManager {
	
	private static final Class<CiscoExtension> EXTENSION_CLASS = CiscoExtension.class;
	
	public CiscoExtensionManager() {
		super(EXTENSION_CLASS);
	}
	
	@Override
	public final Object getValue(String key) throws IOException {
		if (key.equals(CiscoExtension.MODEL_NUMBER)) {
			return getModelNumber();
		} else if (key.equals(CiscoExtension.SYSTEM_SERIAL_NUMBER)) {
			return getSystemSerialNumber();
		} else if (key.equals(CiscoExtension.PROCESSOR_STRING)) {
			return getProcessorString();
		} else if (key.equals(CiscoExtension.CDP_NEIGHBORS)) {
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