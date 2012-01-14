package at.netcrawler.network.connection.ssh.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.SSHSettings;

import com.mindbright.ssh2.SSH2ConsoleRemote;
import com.mindbright.ssh2.SSH2Exception;
import com.mindbright.ssh2.SSH2SimpleClient;
import com.mindbright.ssh2.SSH2Transport;
import com.mindbright.util.SecureRandomAndPad;


public class LocalSSH2ConnectionImpl extends LocalSSHConnectionImpl {
	
	private static final String DISCONNECT_MESSAGE = "close";
	
	private SSH2SimpleClient client;
	private SSH2ConsoleRemote console;
	
	public LocalSSH2ConnectionImpl(IPDeviceAccessor accessor,
			SSHSettings settings) throws IOException {
		super(accessor, settings);
		
		try {
			Socket socket = new Socket(accessor.getInetAddress(),
					settings.getPort());
			SSH2Transport transport = new SSH2Transport(socket,
					new SecureRandomAndPad());
			client = new SSH2SimpleClient(transport, settings.getUsername(),
					settings.getPassword());
			console = new SSH2ConsoleRemote(client.getConnection());
			if (!console.shell(true)) throw new IOException(
					"couldn't start a shell session!");
		} catch (SSH2Exception e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return console.getStdOut();
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		return console.getStdIn();
	}
	
	@Override
	public void closeImpl() throws IOException {
		console.close(true);
		client.getTransport().normalDisconnect(DISCONNECT_MESSAGE);
	}
	
}