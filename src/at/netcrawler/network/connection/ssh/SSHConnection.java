package at.netcrawler.network.connection.ssh;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.CommandLineConnection;
import at.netcrawler.network.connection.ConnectionType;
import at.netcrawler.network.connection.TCPIPDeviceConnection;


public abstract class SSHConnection extends TCPIPDeviceConnection<SSHSettings> implements
		SSHClient,
		CommandLineConnection {
	
	private SSHVersion version;
	
	public SSHConnection(IPDeviceAccessor accessor, SSHSettings settings) {
		super(accessor, settings);
	}
	
	@Override
	public final ConnectionType getConnectionType() {
		return ConnectionType.SSH;
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