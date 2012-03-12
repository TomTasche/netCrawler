package at.netcrawler.network.connection.ssh.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.SSHConnection;
import at.netcrawler.network.connection.ssh.SSHSettings;


public abstract class LocalSSHConnectionImpl extends SSHConnection {
	
	public static LocalSSHConnectionImpl getInstance(IPDeviceAccessor accessor,
			SSHSettings settings) throws IOException {
		switch (settings.getVersion()) {
		case VERSION1:
			return new LocalSSH1ConnectionImpl(accessor, settings);
		case VERSION2:
			return new LocalSSH2ConnectionImpl(accessor, settings);
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public LocalSSHConnectionImpl(IPDeviceAccessor accessor,
			SSHSettings settings) throws IOException {
		super(accessor, settings);
	}
	
	@Override
	public abstract InputStream getInputStream() throws IOException;
	
	@Override
	public abstract OutputStream getOutputStream() throws IOException;
	
	@Override
	protected abstract void closeImpl() throws IOException;
	
}