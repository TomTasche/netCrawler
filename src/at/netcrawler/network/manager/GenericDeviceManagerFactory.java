package at.netcrawler.network.manager;

import java.io.IOException;

import at.netcrawler.network.connection.Connection;
import at.netcrawler.network.model.NetworkDevice;


public abstract class GenericDeviceManagerFactory<M extends DeviceManager, C extends Connection> implements
		DeviceManagerFactory {
	
	@Override
	@SuppressWarnings("unchecked")
	public final M buildDeviceManager(NetworkDevice device,
			Connection connection) throws IOException {
		return buildDeviceManagerGeneric(device, (C) connection);
	}
	
	public abstract M buildDeviceManagerGeneric(NetworkDevice device,
			C connection) throws IOException;
	
}