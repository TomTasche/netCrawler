package network.ssh;

import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;


public class SimpleSSH2Executor extends SimpleSSHExecutor {
	
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	
	private String host;
	private int port;
	private String username;
	private String password;
	
	private JSch jsch;
	private Session session;
	private ChannelExec channel;
	
	
	public SimpleSSH2Executor(String login, String password) {
		this(login.split("@")[1], DEFAULT_PORT, login.split("@")[0], password);
	}
	public SimpleSSH2Executor(String host, String user, String password) {
		this(host, DEFAULT_PORT, user, password);
	}
	public SimpleSSH2Executor(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		
		jsch = new JSch();
	}
	
	
	public int getLastExitStatus() {
		return channel.getExitStatus();
	}
	
	
	public String execute(String command) throws JSchException, IOException {
		session = jsch.getSession(username, host, port);
		session.setUserInfo(new SimpleUserInfo());
		session.connect();
		session.setDaemonThread(false);
		
		channel = (ChannelExec) session.openChannel("exec");
		channel.setCommand(command);
		channel.connect();
		
		StringBuilder builder = new StringBuilder();
		InputStream errorInputStream = channel.getErrStream();
		InputStream inputStream = channel.getInputStream();
		int read;
		
		while ((read = errorInputStream.read()) != -1) {
			System.out.println("error read " + read);
			builder.append((char) read);
		}
		
		if (builder.length() > 0) builder.append(LINE_SEPARATOR);
		
		while ((read = inputStream.read()) != -1) {
			builder.append((char) read);
		}
		
		channel.disconnect();
		session.disconnect();
		
		return builder.toString();
	}
	
	
	private class SimpleUserInfo implements UserInfo {
		public String getPassword() { return password; }
		public String getPassphrase() { return null; }
		
		public boolean promptYesNo(String str){ return true; }
		public boolean promptPassphrase(String message) { return true; }
		public boolean promptPassword(String message) { return true; }
		
		public void showMessage(String message) {}
	}
	
}