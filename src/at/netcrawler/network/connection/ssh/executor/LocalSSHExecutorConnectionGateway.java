package at.netcrawler.network.connection.ssh.executor;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.DeviceConnection;
import at.netcrawler.network.connection.ssh.console.LocalSSHConsoleConnection;


public class LocalSSHExecutorConnectionGateway extends
		SSHExecutorConnectionGateway {
	
	@Override
	public Class<? extends DeviceConnection> getDeviceConnectionClass() {
		return LocalSSHConsoleConnection.class;
	}
	
	@Override
	public LocalSSHExecutorConnection openConnection(IPDeviceAccessor accessor,
			SSHExecutorConnectionSettings settings) throws IOException {
		return new LocalSSHExecutorConnection(accessor, settings);
	}
	
}