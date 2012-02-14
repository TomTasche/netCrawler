package at.netcrawler.network.connection.ssh;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.TCPIPDeviceConnection;


public abstract class SSHConnection extends TCPIPDeviceConnection implements
		SSHClient {
	
	private SSHVersion version;
	
	public SSHConnection(IPDeviceAccessor accessor, SSHSettings settings) {
		super(accessor, settings);
	}
	
	@Override
	public SSHVersion getVersion() {
		return version;
	}
	
	protected void connectGenericImpl(IPDeviceAccessor accessor,
			SSHSettings settings) throws IOException {
		version = settings.getVersion();
	}
	
}