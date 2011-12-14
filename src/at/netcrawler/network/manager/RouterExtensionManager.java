package at.netcrawler.network.manager;

import java.io.IOException;
import java.util.Set;

import at.andiwand.library.util.GenericsUtil;
import at.netcrawler.network.Capability;
import at.netcrawler.network.RoutingTable;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.extension.RouterExtension;


public abstract class RouterExtensionManager<M extends DeviceManager> extends
		GenericDeviceExtensionManager<M> {
	
	private static final Class<RouterExtension> EXTENSION_CLASS = RouterExtension.class;
	
	public RouterExtensionManager() {
		super(EXTENSION_CLASS);
	}
	
	public abstract RoutingTable getRoutingTable() throws IOException;
	
	@Override
	public boolean hasExtension() throws IOException {
		NetworkDevice device = getDevice();
		Set<Capability> capabilities = GenericsUtil.castObject(device
				.getValue(NetworkDevice.CAPABILITIES));
		
		return capabilities.contains(Capability.ROUTER);
	}
	
	@Override
	public void readDeviceExtension() throws IOException {
		NetworkDevice device = getDevice();
		
		device.setValue(RouterExtension.ROUTING_TABLE, getRoutingTable());
	}
	
}