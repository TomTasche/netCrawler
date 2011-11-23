package at.netcrawler.network.controller;

import java.io.IOException;

import at.netcrawler.network.RoutingTable;
import at.netcrawler.network.model.extension.RouterExtension;


public abstract class RouterExtensionSetManager extends DeviceExtensionSetManager {
	
	public RouterExtensionSetManager(DeviceManager deviceManager) {
		super(deviceManager);
	}
	
	
	public abstract RoutingTable getRoutingTable() throws IOException;
	
	@Override
	public void fetchDeviceExtensionSet() throws IOException {
		device.setValue(RouterExtension.ROUTING_TABLE, getRoutingTable());
	}
	
}