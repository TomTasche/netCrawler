package at.andiwand.library.network.ssh;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.mindbright.jca.security.interfaces.RSAPublicKey;
import com.mindbright.ssh.SSH;
import com.mindbright.ssh.SSHAuthenticator;
import com.mindbright.ssh.SSHClientUser;
import com.mindbright.ssh.SSHConsoleClient;
import com.mindbright.ssh.SSHInteractorAdapter;
import com.mindbright.ssh.SSHRSAKeyFile;
import com.mindbright.sshcommon.TimeoutException;


public class SSH1Executor extends SSHExecutor {
	
	public static final int DEFAULT_TIMEOUT = 1000;
	
	
	private SSHConsoleClient client;
	
	private int lastExitStatus;
	
	
	public SSH1Executor() {}
	public SSH1Executor(String login, String password) throws Exception {
		super(login.split("@")[1], DEFAULT_PORT, login.split("@")[0], password);
	}
	public SSH1Executor(InetAddress address, String username, String password) throws Exception {
		super(address, username, password);
	}
	public SSH1Executor(String host, String username, String password) throws Exception {
		super(host, username, password);
	}
	public SSH1Executor(InetSocketAddress socketAddress, String username, String password) throws Exception {
		super(socketAddress, username, password);
	}
	public SSH1Executor(String host, int port, String username, String password) throws Exception {
		super(host, port, username, password);
	}
	public SSH1Executor(InetAddress address, int port, String username, String password) throws Exception {
		super(address, port, username, password);
	}
	
	
	@Override
	public void connect(InetAddress address, int port, String username, String password) throws Exception {
		client = new SSHConsoleClient(address.getHostAddress(), port,
				new SimpleAuthenticator(username, password), new SSHInteractorAdapter());
	}
	
	@Override
	public void close() {
		client.close();
	}
	
	
	@Override
	public String execute(String command) throws Exception {
		return execute(command, DEFAULT_TIMEOUT);
	}
	public String execute(String command, long timeout) throws IOException, TimeoutException {
		lastExitStatus = client.command(command, timeout);
		
		StringBuilder builder = new StringBuilder();
		Reader reader = new InputStreamReader(client.getStdOut());
		int read;
		
		while ((read = reader.read()) != -1) {
			builder.append((char) read);
		}
		
		reader.close();
		
		return builder.toString();
	}
	
	@Override
	public int lastExitStatus() {
		return lastExitStatus;
	}
	
	
	private class SimpleAuthenticator implements SSHAuthenticator {
		private String username;
		private String password;
		
		public SimpleAuthenticator(String username, String password) {
			this.username = username;
			this.password = password;
		}
		
		public String getUsername(SSHClientUser origin) throws IOException {
			return username;
		}
		public String getPassword(SSHClientUser origin) throws IOException {
			return password;
		}
		
		public String getChallengeResponse(SSHClientUser origin, String challenge) throws IOException {
			return null;
		}
		public int[] getAuthTypes(SSHClientUser origin) {
			return SSH.getAuthTypes("password");
		}
		public int getCipher(SSHClientUser origin) {
			return SSH.CIPHER_ANY;
		}
		public SSHRSAKeyFile getIdentityFile(SSHClientUser origin) throws IOException {
			return null;
		}
		public String getIdentityPassword(SSHClientUser origin) throws IOException {
			return null;
		}
		public boolean verifyKnownHosts(RSAPublicKey hostPub) throws IOException {
			return true;
		}
	}
	
}