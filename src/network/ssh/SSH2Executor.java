package network.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;


public class SSH2Executor extends SSHExecutor {
	
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	
	private Session session;
	private ChannelExec channel;
	
	
	public SSH2Executor() {}
	public SSH2Executor(String login, String password) throws Exception {
		super(login.split("@")[1], DEFAULT_PORT, login.split("@")[0], password);
	}
	public SSH2Executor(InetAddress address, String username, String password) throws Exception {
		super(address, username, password);
	}
	public SSH2Executor(String host, String username, String password) throws Exception {
		super(host, username, password);
	}
	public SSH2Executor(InetSocketAddress socketAddress, String username, String password) throws Exception {
		super(socketAddress, username, password);
	}
	public SSH2Executor(String host, int port, String username, String password) throws Exception {
		super(host, port, username, password);
	}
	public SSH2Executor(InetAddress address, int port, String username, String password) throws Exception {
		super(address, port, username, password);
	}
	
	
	public int getLastExitStatus() {
		return channel.getExitStatus();
	}
	
	
	@Override
	public void connect(InetAddress address, int port, String username, String password) throws Exception {
		JSch jsch = new JSch();
		
		session = jsch.getSession(username, address.getHostAddress(), port);
		session.setUserInfo(new SimpleUserInfo(password));
		session.setDaemonThread(true);
		session.connect();
	}
	
	@Override
	public void close() {
		channel.disconnect();
		session.disconnect();
	}
	
	
	public String execute(String command) throws JSchException, IOException {
		channel = (ChannelExec) session.openChannel("exec");
		channel.setCommand(command);
		channel.connect();
		
		StringBuilder builder = new StringBuilder();
		InputStream errorInputStream = channel.getErrStream();
		InputStream inputStream = channel.getInputStream();
		int read;
		
		while ((read = errorInputStream.read()) != -1) {
			builder.append((char) read);
		}
		
		if (builder.length() > 0) builder.append(LINE_SEPARATOR);
		
		while ((read = inputStream.read()) != -1) {
			builder.append((char) read);
		}
		
		channel.disconnect();
		
		return builder.toString();
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