package at.netcrawler.network.connection.ssh.console;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.console.impl.LocalSSHConsoleConnectionImpl;


public class LocalSSHConsoleConnection extends SSHConsoleConnection {
	
	private LocalSSHConsoleConnectionImpl client;
	
	public LocalSSHConsoleConnection(IPDeviceAccessor accessor,
			SSHConsoleConnectionSettings settings) throws IOException {
		super(accessor, settings);
		
		client = LocalSSHConsoleConnectionImpl.getInstance(accessor, settings);
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return client.getInputStream();
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		return client.getOutputStream();
	}
	
	@Override
	protected void closeImpl() throws IOException {
		client.close();
	}
	
}