package at.netcrawler.network.connection.ssh.executor.impl;

import java.io.IOException;

import at.andiwand.library.util.StreamUtil;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.executor.SSHExecutorConnectionSettings;

import com.mindbright.jca.security.interfaces.RSAPublicKey;
import com.mindbright.ssh.SSH;
import com.mindbright.ssh.SSHAuthenticator;
import com.mindbright.ssh.SSHClientUser;
import com.mindbright.ssh.SSHConsoleClient;
import com.mindbright.ssh.SSHInteractorAdapter;
import com.mindbright.ssh.SSHRSAKeyFile;
import com.mindbright.sshcommon.TimeoutException;


public class LocalSSH1ExecutorConnectionImpl extends
		LocalSSHExecutorConnectionImpl {
	
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
	
	private SSHConsoleClient client;
	
	private int timeout;
	
	private int lastExitStatus;
	
	public LocalSSH1ExecutorConnectionImpl(IPDeviceAccessor accessor,
			SSHExecutorConnectionSettings settings) throws IOException {
		super(accessor, settings);
		
		client = new SSHConsoleClient(accessor.getIpAddress().toString(),
				settings.getPort(), new SimpleAuthenticator(settings
						.getUsername(), settings.getPassword()),
				new SSHInteractorAdapter());
		
		timeout = settings.getTimeout();
	}
	
	@Override
	public String execute(String command) throws IOException {
		try {
			lastExitStatus = client.command(command, timeout);
			return StreamUtil.readStream(client.getStdOut());
		} catch (TimeoutException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public int getLastExitStatus() {
		return lastExitStatus;
	}
	
	@Override
	protected void closeImpl() throws IOException {
		client.close();
	}
	
}