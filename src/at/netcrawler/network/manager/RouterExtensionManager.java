package at.netcrawler.network.manager;

import java.io.IOException;

import at.netcrawler.network.RoutingTable;
import at.netcrawler.network.connection.DeviceConnection;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.extension.RouterExtension;


public abstract class RouterExtensionManager<C extends DeviceConnection>
		extends DeviceExtensionManager<C> {
	
	public RouterExtensionManager() {
		super(RouterExtension.class);
	}
	
	public abstract RoutingTable getRoutingTable() throws IOException;
	
	@Override
	public void readDeviceExtension() throws IOException {
		NetworkDevice device = getDevice();
		
		device.setValue(RouterExtension.ROUTING_TABLE, getRoutingTable());
	}
	
}