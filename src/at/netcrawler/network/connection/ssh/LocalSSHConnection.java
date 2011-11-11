package at.netcrawler.network.connection.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.netcrawler.network.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.deprecated.D_SSH1Client;
import at.netcrawler.network.connection.ssh.deprecated.D_SSH2Client;
import at.netcrawler.network.connection.ssh.deprecated.D_SSHClient;


public class LocalSSHConnection extends SSHConnection {
	
	private D_SSHClient client;
	
	public LocalSSHConnection(IPDeviceAccessor accessor,
			SSHConnectionSettings settings) throws IOException {
		super(accessor, settings);
		
		switch (version) {
		case VERSION1:
			client = new D_SSH1Client(accessor.getInetAddress(),
					settings.getUsername(), settings.getPassword());
			break;
		case VERSION2:
			client = new D_SSH2Client(accessor.getInetAddress(),
					settings.getUsername(), settings.getPassword());
			break;
		}
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