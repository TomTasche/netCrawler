package at.netcrawler.network.connection.ssh.console.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.console.SSHConsoleConnectionSettings;

import com.mindbright.ssh2.SSH2ConsoleRemote;
import com.mindbright.ssh2.SSH2Exception;
import com.mindbright.ssh2.SSH2SimpleClient;
import com.mindbright.ssh2.SSH2Transport;
import com.mindbright.util.SecureRandomAndPad;


public class LocalSSH2ConsoleConnectionImpl extends
		LocalSSHConsoleConnectionImpl {
	
	private static final String DISCONNECT_MESSAGE = "close";
	
	private Socket socket;
	private SSH2Transport transport;
	private SSH2ConsoleRemote console;
	
	public LocalSSH2ConsoleConnectionImpl(IPDeviceAccessor accessor,
			SSHConsoleConnectionSettings settings) throws IOException {
		super(accessor, settings);
		
		try {
			socket = new Socket(accessor.getInetAddress(), settings.getPort());
			transport = new SSH2Transport(socket, new SecureRandomAndPad());
			SSH2SimpleClient client = new SSH2SimpleClient(transport, settings
					.getUsername(), settings.getPassword());
			console = new SSH2ConsoleRemote(client.getConnection(), null, null);
		} catch (SSH2Exception e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public InputStream getInputStream() {
		return console.getStdOut();
	}
	
	@Override
	public OutputStream getOutputStream() {
		return console.getStdIn();
	}
	
	@Override
	protected void closeImpl() throws IOException {
		console.close();
		transport.normalDisconnect(DISCONNECT_MESSAGE);
		socket.close();
	}
	
}