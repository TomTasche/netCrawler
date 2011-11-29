package at.netcrawler.network.connection.ssh;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.IPDeviceConnection;


public abstract class SSHConnection extends IPDeviceConnection implements
		SSHClient {
	
	protected final SSHVersion version;
	
	public SSHConnection(IPDeviceAccessor accessor,
			SSHConnectionSettings settings) {
		super(accessor, settings);
		
		version = settings.getVersion();
	}
	
	@Override
	public SSHVersion getVersion() {
		return version;
	}
	
}