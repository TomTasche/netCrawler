package at.netcrawler.network.manager;

import java.io.IOException;

import at.netcrawler.network.model.extension.CiscoRouterExtension;


public abstract class CiscoRouterExtensionManager extends
		DeviceExtensionManager {
	
	private static final Class<CiscoRouterExtension> EXTENSION_CLASS = CiscoRouterExtension.class;
	
	public CiscoRouterExtensionManager() {
		super(EXTENSION_CLASS);
	}
	
	@Override
	public final Object getValue(String key) throws IOException {
		if (key.equals(CiscoRouterExtension.PROCESSOR_BOARD_ID)) {
			return getProcessorBoardId();
		}
		
		throw new IllegalArgumentException("Unsupported key!");
	}
	
	protected abstract String getProcessorBoardId() throws IOException;
	
	@Override
	public final boolean setValue(String key, Object value) throws IOException {
		throw new IllegalArgumentException("Unsupported key!");
	}
	
	// TODO: implement
	@Override
	public boolean hasExtension() throws IOException {
		return false;
	}
	
}