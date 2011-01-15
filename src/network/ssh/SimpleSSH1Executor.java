package network.ssh;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.mindbright.jca.security.interfaces.RSAPublicKey;
import com.mindbright.ssh.SSH;
import com.mindbright.ssh.SSHAuthenticator;
import com.mindbright.ssh.SSHClientUser;
import com.mindbright.ssh.SSHConsoleClient;
import com.mindbright.ssh.SSHInteractorAdapter;
import com.mindbright.ssh.SSHRSAKeyFile;


public class SimpleSSH1Executor extends SimpleSSHExecutor {
	
	private String host;
	private int port;
	private String username;
	private String password;
	
	private boolean lastSuccess;
	
	
	public SimpleSSH1Executor(String host, String username, String password) {
		this(host, DEFAULT_PORT, username, password);
	}
	public SimpleSSH1Executor(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	
	public boolean getLastSuccess() {
		return lastSuccess;
	}
	
	
	public String execute(String command) throws IOException {
		SSHConsoleClient client = new SSHConsoleClient(host, port, new SimpleAuthenticator(), new SSHInteractorAdapter());
		lastSuccess = client.command(command);
		
		String result = "";
		
		Reader reader = new InputStreamReader(client.getStdOut());
		
		int i;
		while ((i = reader.read()) != -1) {
			result += (char) i;
		}
		
		reader.close();
		client.close();
		
		return result;
	}
	
	
	private class SimpleAuthenticator implements SSHAuthenticator {
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