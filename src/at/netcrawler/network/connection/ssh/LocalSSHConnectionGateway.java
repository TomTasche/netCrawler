package at.netcrawler.network.connection.ssh;

import java.io.IOException;

import at.netcrawler.network.IPDeviceAccessor;
import at.netcrawler.network.connection.DeviceConnection;


public class LocalSSHConnectionGateway extends SSHConnectionGateway {
	
	@Override
	public Class<? extends DeviceConnection> getDeviceConnectionClass() {
		return LocalSSHConnection.class;
	}
	
	@Override
	public LocalSSHConnection openConnection(IPDeviceAccessor accessor,
			SSHConnectionSettings settings) throws IOException {
		return new LocalSSHConnection(accessor, settings);
	}
	
}