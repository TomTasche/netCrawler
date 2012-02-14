package at.netcrawler.network.manager;

import java.io.IOException;
import java.util.Set;

import at.andiwand.library.util.GenericsUtil;
import at.netcrawler.network.Capability;
import at.netcrawler.network.RoutingTable;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.extension.RouterExtension;


public abstract class RouterExtensionManager extends DeviceExtensionManager {
	
	private static final Class<RouterExtension> EXTENSION_CLASS = RouterExtension.class;
	
	public RouterExtensionManager() {
		super(EXTENSION_CLASS);
	}
	
	@Override
	public final Object getValue(String key) throws IOException {
		if (key.equals(RouterExtension.ROUTING_TABLE)) {
			return getRoutingTable();
		}
		
		throw new IllegalArgumentException("Unsupported key!");
	}
	
	protected abstract RoutingTable getRoutingTable() throws IOException;
	
	@Override
	public final boolean setValue(String key, Object value) throws IOException {
		throw new IllegalArgumentException("Unsupported key!");
	}
	
	@Override
	public boolean hasExtension() throws IOException {
		NetworkDevice device = getDevice();
		Set<Capability> capabilities = GenericsUtil.castObject(device
				.getValue(NetworkDevice.CAPABILITIES));
		
		return capabilities.contains(Capability.ROUTER);
	}
	
}