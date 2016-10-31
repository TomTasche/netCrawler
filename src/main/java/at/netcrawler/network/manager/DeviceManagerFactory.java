package at.netcrawler.network.manager;

import java.io.IOException;

import at.netcrawler.network.connection.Connection;
import at.netcrawler.network.model.NetworkDevice;


public interface DeviceManagerFactory {
	
	public DeviceManager buildDeviceManager(NetworkDevice device,
			Connection connection) throws IOException;
	
}