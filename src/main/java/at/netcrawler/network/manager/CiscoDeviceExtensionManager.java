package at.netcrawler.network.manager;

import java.io.IOException;
import java.util.List;

import at.netcrawler.network.model.CDPNeighbor;
import at.netcrawler.network.model.extension.CiscoDeviceExtension;


public abstract class CiscoDeviceExtensionManager extends
		DeviceExtensionManager {
	
	private static final Class<CiscoDeviceExtension> EXTENSION_CLASS = CiscoDeviceExtension.class;
	
	public CiscoDeviceExtensionManager() {
		super(EXTENSION_CLASS);
	}
	
	@Override
	public final Object fetchValue(String key) throws IOException {
		if (key.equals(CiscoDeviceExtension.CDP_NEIGHBORS)) {
			return getCDPNeighbors();
		}
		
		throw new IllegalArgumentException("Unsupported key " + key);
	}
	
	protected abstract List<CDPNeighbor> getCDPNeighbors() throws IOException;
	
	@Override
	public final boolean setValue(String key, Object value) throws IOException {
		throw new IllegalArgumentException("Unsupported key " + key);
	}
	
	// TODO: implement
	@Override
	public boolean hasExtension() throws IOException {
		return false;
	}
	
}