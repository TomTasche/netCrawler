package at.netcrawler.network.connection.ssh.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.LocalSSHConnection;
import at.netcrawler.network.connection.ssh.SSHSettings;


public abstract class LocalSSHConnectionImpl extends LocalSSHConnection {
	
	public static LocalSSHConnectionImpl getInstance(IPDeviceAccessor accessor,
			SSHSettings settings) throws IOException {
		LocalSSHConnectionImpl result;
		
		switch (settings.getVersion()) {
		case VERSION1:
			result = new LocalSSH1ConnectionImpl();
			break;
		case VERSION2:
			result = new LocalSSH2ConnectionImpl();
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
		
		result.connect(accessor, settings);
		
		return result;
	}
	
	@Override
	public abstract InputStream getInputStream() throws IOException;
	
	@Override
	public abstract OutputStream getOutputStream() throws IOException;
	
	@Override
	protected abstract void closeImpl() throws IOException;
	
}