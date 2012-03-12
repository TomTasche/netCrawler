package at.netcrawler.network.connection.ssh.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.SSHSettings;

import com.mindbright.jca.security.interfaces.RSAPublicKey;
import com.mindbright.ssh.SSH;
import com.mindbright.ssh.SSHAuthenticator;
import com.mindbright.ssh.SSHClientUser;
import com.mindbright.ssh.SSHConsoleClient;
import com.mindbright.ssh.SSHInteractorAdapter;
import com.mindbright.ssh.SSHRSAKeyFile;


public class LocalSSH1ConnectionImpl extends LocalSSHConnectionImpl {
	
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
		
		public String getChallengeResponse(SSHClientUser origin,
				String challenge) throws IOException {
			return null;
		}
		
		public int[] getAuthTypes(SSHClientUser origin) {
			return SSH.getAuthTypes("password");
		}
		
		public int getCipher(SSHClientUser origin) {
			return SSH.CIPHER_ANY;
		}
		
		public SSHRSAKeyFile getIdentityFile(SSHClientUser origin)
				throws IOException {
			return null;
		}
		
		public String getIdentityPassword(SSHClientUser origin)
				throws IOException {
			return null;
		}
		
		public boolean verifyKnownHosts(RSAPublicKey hostPub)
				throws IOException {
			return true;
		}
	}
	
	private final SSHConsoleClient client;
	
	public LocalSSH1ConnectionImpl(IPDeviceAccessor accessor,
			SSHSettings settings) throws IOException {
		super(accessor, settings);
		
		client = new SSHConsoleClient(accessor.getIpAddress().toString(),
				settings.getPort(), new SimpleAuthenticator(settings
						.getUsername(), settings.getPassword()),
				new SSHInteractorAdapter());
		if (!client.shell())
			throw new IOException("couldn't start a shell session!");
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
	protected void closeImpl() throws IOException {
		client.close();
	}
	
}