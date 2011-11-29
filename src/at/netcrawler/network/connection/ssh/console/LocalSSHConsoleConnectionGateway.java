package at.netcrawler.network.connection.ssh.console;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.DeviceConnection;
import at.netcrawler.network.connection.ssh.console.impl.LocalSSHConsoleConnectionImpl;


public class LocalSSHConsoleConnectionGateway extends
		SSHConsoleConnectionGateway {
	
	@Override
	public Class<? extends DeviceConnection> getDeviceConnectionClass() {
		return LocalSSHConsoleConnection.class;
	}
	
	@Override
	public LocalSSHConsoleConnection openConnection(IPDeviceAccessor accessor,
			SSHConsoleConnectionSettings settings) throws IOException {
		return LocalSSHConsoleConnectionImpl.getInstance(accessor, settings);
	}
	
}