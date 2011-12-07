package at.netcrawler.network.connection.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.impl.LocalSSHConnectionImpl;


public class LocalSSHConnection extends SSHConnection {
	
	private LocalSSHConnectionImpl client;
	
	@Override
	protected void connectGenericImpl(IPDeviceAccessor accessor,
			SSHSettings settings) throws IOException {
		super.connectGenericImpl(accessor, settings);
		
		client = LocalSSHConnectionImpl.getInstance(accessor, settings);
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