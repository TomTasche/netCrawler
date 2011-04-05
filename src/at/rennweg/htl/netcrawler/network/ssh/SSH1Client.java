package at.rennweg.htl.netcrawler.network.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.mindbright.jca.security.interfaces.RSAPublicKey;
import com.mindbright.ssh.SSH;
import com.mindbright.ssh.SSHAuthenticator;
import com.mindbright.ssh.SSHClientUser;
import com.mindbright.ssh.SSHConsoleClient;
import com.mindbright.ssh.SSHInteractorAdapter;
import com.mindbright.ssh.SSHRSAKeyFile;


public class SSH1Client extends SSHClient {
	
	public static final int DEFAULT_TIMEOUT = 1000;
	
	
	
	
	private SSHConsoleClient client;
	
	
	
	public SSH1Client() {}
	public SSH1Client(String login, String password) throws Exception {
		super(login, password);
	}
	public SSH1Client(InetAddress address, String username, String password) throws Exception {
		super(address, username, password);
	}
	public SSH1Client(String host, String username, String password) throws Exception {
		super(host, username, password);
	}
	public SSH1Client(InetSocketAddress socketAddress, String username, String password) throws Exception {
		super(socketAddress, username, password);
	}
	public SSH1Client(String host, int port, String username, String password) throws Exception {
		super(host, port, username, password);
	}
	public SSH1Client(InetAddress address, int port, String username, String password) throws Exception {
		super(address, port, username, password);
	}
	
	
	
	@Override
	public InputStream getInputStream() throws IOException {
		return client.getStdOut();
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		return client.getStdIn();
	}
	
	
	@Override
	public void connect(InetAddress address, int port, String username, String password) throws Exception {
		client = new SSHConsoleClient(address.getHostAddress(), port,
				new SimpleAuthenticator(username, password), new SSHInteractorAdapter());
		
		if (!client.shell()) throw new IOException("Was not able to connect!");
	}
	
	@Override
	public void close() {
		client.close();
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