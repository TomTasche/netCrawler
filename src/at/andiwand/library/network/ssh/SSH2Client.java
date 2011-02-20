package at.andiwand.library.network.ssh;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;


public class SSH2Client extends SSHClient {
	
	private Session session;
	private Channel channel;
	private PipedInputStream inputStream;
	private PipedOutputStream outputStream;
	
	
	public SSH2Client() {}
	public SSH2Client(String login, String password) throws Exception {
		super(login.split("@")[1], DEFAULT_PORT, login.split("@")[0], password);
	}
	public SSH2Client(InetAddress address, String username, String password) throws Exception {
		super(address, username, password);
	}
	public SSH2Client(String host, String username, String password) throws Exception {
		super(host, username, password);
	}
	public SSH2Client(InetSocketAddress socketAddress, String username, String password) throws Exception {
		super(socketAddress, username, password);
	}
	public SSH2Client(String host, int port, String username, String password) throws Exception {
		super(host, port, username, password);
	}
	public SSH2Client(InetAddress address, int port, String username, String password) throws Exception {
		super(address, port, username, password);
	}
	
	
	public Channel getChannel() {
		return channel;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	
	@Override
	public void connect(InetAddress address, int port, String username, String password) throws Exception {
		JSch jsch = new JSch();
		inputStream = new PipedInputStream();
		outputStream = new PipedOutputStream();
		
		session = jsch.getSession(username, address.getHostAddress(), port);
		session.setUserInfo(new SimpleUserInfo(password));
		session.setDaemonThread(true);
		session.connect();
		
		channel = session.openChannel("shell");
		channel.setInputStream(new PipedInputStream(outputStream));
		channel.setOutputStream(new PipedOutputStream(inputStream));
		channel.connect();
	}
	
	@Override
	public void close() {
		channel.disconnect();
		session.disconnect();
	}
	
	
	public class SimpleUserInfo implements UserInfo {
		private String password;
		
		public SimpleUserInfo(String password) {
			this.password = password;
		}
		
		public String getPassword() { return password; }
		public String getPassphrase() { return null; }
		
		public boolean promptYesNo(String str){ return true; }
		public boolean promptPassphrase(String message) { return true; }
		public boolean promptPassword(String message) { return true; }
		
		public void showMessage(String message) {}
	}
	
}