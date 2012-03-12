package at.netcrawler.network.manager;

import java.io.IOException;

import at.netcrawler.network.Capability;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.extension.CiscoDeviceExtension;
import at.netcrawler.network.model.extension.CiscoSwitchExtension;


public abstract class CiscoSwitchExtensionManager extends
		DeviceExtensionManager {
	
	private static final Class<CiscoSwitchExtension> EXTENSION_CLASS = CiscoSwitchExtension.class;
	
	public CiscoSwitchExtensionManager() {
		super(EXTENSION_CLASS);
	}
	
	@Override
	public final Object getValue(String key) throws IOException {
		if (key.equals(CiscoSwitchExtension.MODEL_NUMBER)) {
			return getModelNumber();
		} else if (key.equals(CiscoSwitchExtension.SYSTEM_SERIAL_NUMBER)) {
			return getSystemSerialNumber();
		}
		
		throw new IllegalArgumentException("Unsupported key!");
	}
	
	protected abstract String getModelNumber() throws IOException;
	
	protected abstract String getSystemSerialNumber() throws IOException;
	
	@Override
	public final boolean setValue(String key, Object value) throws IOException {
		throw new IllegalArgumentException("Unsupported key!");
	}
	
	// TODO: implement
	@Override
	public boolean hasExtension() throws IOException {
		return device.containsExtension(CiscoDeviceExtension.EXTENSION)
				&& device.getValue(NetworkDevice.MAJOR_CAPABILITY).equals(
						Capability.SWITCH);
	}
	
}