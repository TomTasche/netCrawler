package at.netcrawler.test;

import java.net.Socket;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import at.andiwand.library.io.StreamUtil;

import com.mindbright.ssh2.SSH2ConsoleRemote;
import com.mindbright.ssh2.SSH2SimpleClient;
import com.mindbright.ssh2.SSH2Transport;
import com.mindbright.util.SecureRandomAndPad;


public class MindtermSSH2Test {
	
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
		
		SecureRandomAndPad secureRandom = new SecureRandomAndPad();
		
		Socket serverSocket = new Socket(address, port);
		SSH2Transport transport = new SSH2Transport(serverSocket, secureRandom);
		SSH2SimpleClient client = new SSH2SimpleClient(transport, username,
				password);
		SSH2ConsoleRemote console = new SSH2ConsoleRemote(
				client.getConnection(), null, null);
		
		System.out.println(console.command("pwd"));
		System.out.println(console.waitForExitStatus());
		System.out.println(StreamUtil.read(console.getStdOut()));
		System.out.println();
		
		System.out.println(console.command("ssh andreas@localhost"));
		System.out.println(console.waitForExitStatus());
		System.out.println(StreamUtil.read(console.getStdOut()));
		System.out.println();
		
		console.close();
		transport.normalDisconnect("quit");
		System.out.println("exit");
	}
	
}