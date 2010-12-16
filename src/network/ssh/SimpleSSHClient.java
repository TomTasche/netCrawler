package network.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;


public class SimpleSSHClient {
	
	public static final int DEFAULT_PORT = 22;
	
	
	private String host;
	private int port;
	private String username;
	private String password;
	
	private JSch jsch;
	private Session session;
	private Channel channel;
	private PipedInputStream inputStream;
	private PipedOutputStream outputStream;
	
	
	public SimpleSSHClient(String login, String password) {
		this(login.split("@")[1], DEFAULT_PORT, login.split("@")[0], password);
	}
	public SimpleSSHClient(String host, String user, String password) {
		this(host, DEFAULT_PORT, user, password);
	}
	public SimpleSSHClient(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		
		jsch = new JSch();
		
		inputStream = new PipedInputStream();
		outputStream = new PipedOutputStream();
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
	
	
	public void connect() throws JSchException, IOException {
		session = jsch.getSession(username, host, port);
		session.setUserInfo(new SimpleUserInfo());
		session.connect();
		session.setDaemonThread(false);
		
		channel = session.openChannel("shell");
		channel.setInputStream(new PipedInputStream(outputStream));
		channel.setOutputStream(new PipedOutputStream(inputStream));
		channel.connect();
	}
	
	public void disconnect() {
		channel.disconnect();
		session.disconnect();
	}
	
	
	public class SimpleUserInfo implements UserInfo {
		public String getPassword() { return password; }
		public String getPassphrase() { return null; }
		
		public boolean promptYesNo(String str){ return true; }
		public boolean promptPassphrase(String message) { return true; }
		public boolean promptPassword(String message) { return true; }
		
		public void showMessage(String message) {}
	}
	
}