package at.netcrawler.test;

import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import at.andiwand.library.io.StreamUtil;

import com.mindbright.jca.security.interfaces.RSAPublicKey;
import com.mindbright.ssh.SSH;
import com.mindbright.ssh.SSHAuthenticator;
import com.mindbright.ssh.SSHClientUser;
import com.mindbright.ssh.SSHConsoleClient;
import com.mindbright.ssh.SSHInteractorAdapter;
import com.mindbright.ssh.SSHRSAKeyFile;


public class MindtermSSH1Test {
	
	public static String getPassword(String username) {
		final JPasswordField passwordField = new JPasswordField();
		
		JOptionPane optionPane = new JOptionPane(passwordField,
				JOptionPane.QUESTION_MESSAGE) {
			private static final long serialVersionUID = 646321718244222228L;
			
			public void selectInitialValue() {
				passwordField.requestFocusInWindow();
			}
		};
		
		JDialog dialog = optionPane.createDialog("Password for user: "
				+ username);
		dialog.setVisible(true);
		dialog.dispose();
		
		return new String(passwordField.getPassword());
	}
	
	public static void main(String[] args) throws Throwable {
		String address = "localhost";
		int port = 22;
		String username = "andreas";
		String password = getPassword(username);
		
		SSHConsoleClient client = new SSHConsoleClient(address, port,
				new SimpleAuthenticator(username, password),
				new SSHInteractorAdapter());
		
		System.out.println(client.command("pwd", Integer.MAX_VALUE));
		System.out.println(StreamUtil.readAsString(client.getStdOut()));
		System.out.println();
		
		System.out.println(client.command("ps -ef", Integer.MAX_VALUE));
		System.out.println(StreamUtil.readAsString(client.getStdOut()));
		System.out.println();
		
		client.close();
	}
	
	private static class SimpleAuthenticator implements SSHAuthenticator {
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
	
}