package at.netcrawler.network.connection.ssh;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.IPDeviceConnection;


public abstract class SSHConnection extends IPDeviceConnection<SSHSettings>
		implements SSHClient {
	
	private SSHVersion version;
	
	@Override
	public Class<SSHSettings> getSettingsClass() {
		return SSHSettings.class;
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