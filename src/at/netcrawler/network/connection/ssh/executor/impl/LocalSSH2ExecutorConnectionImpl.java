package at.netcrawler.network.connection.ssh.executor.impl;

import java.io.IOException;
import java.net.Socket;

import at.andiwand.library.util.StreamUtil;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.executor.SSHExecutorConnectionSettings;

import com.mindbright.ssh2.SSH2ConsoleRemote;
import com.mindbright.ssh2.SSH2Exception;
import com.mindbright.ssh2.SSH2SimpleClient;
import com.mindbright.ssh2.SSH2Transport;
import com.mindbright.sshcommon.TimeoutException;
import com.mindbright.util.SecureRandomAndPad;


public class LocalSSH2ExecutorConnectionImpl extends
		LocalSSHExecutorConnectionImpl {

	private static final String DISCONNECT_MESSAGE = "close";
	
	private Socket socket;
	private SSH2Transport transport;
	private SSH2ConsoleRemote console;
	
	private int timeout;
	
	private int lastExitStatus;
	
	public LocalSSH2ExecutorConnectionImpl(IPDeviceAccessor accessor,
			SSHExecutorConnectionSettings settings) throws IOException {
		super(accessor, settings);
		
		try {
			socket = new Socket(accessor.getInetAddress(), settings.getPort());
			transport = new SSH2Transport(socket, new SecureRandomAndPad());
			SSH2SimpleClient client = new SSH2SimpleClient(transport, settings
					.getUsername(), settings.getPassword());
			console = new SSH2ConsoleRemote(client.getConnection(), null, null);
			
			timeout = settings.getTimeout();
		} catch (SSH2Exception e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public String execute(String command) throws IOException {
		try {
			console.command(command);
			lastExitStatus = console.waitForExitStatus(timeout);
			return StreamUtil.readStream(console.getStdOut());
		} catch (TimeoutException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public int getLastExitStatus() {
		return lastExitStatus;
	}
	
	@Override
	public void closeImpl() throws IOException {
		console.close();
		transport.normalDisconnect(DISCONNECT_MESSAGE);
		socket.close();
	}
	
}