package at.netcrawler.test;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.cli.agent.LinuxPromptPatternAgent;
import at.netcrawler.network.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.LocalSSHConnection;
import at.netcrawler.network.connection.ssh.SSHConnectionSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;


public class LinuxPromptPatternAgentTest {
	
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
		
		return new String(passwordField.getPassword());
	}
	
	public static void main(String[] args) throws Throwable {
		SSHVersion version = SSHVersion.VERSION2;
		String address = "127.0.0.1";
		IPv4Address ipAddress = IPv4Address.getByAddress(address);
		int port = 22;
		String username = "andreas";
		String password = getPassword(username);
		
		
		IPDeviceAccessor accessor = new IPDeviceAccessor(ipAddress);
		
		SSHConnectionSettings settings = new SSHConnectionSettings();
		settings.setVersion(version);
		settings.setPort(port);
		settings.setUsername(username);
		settings.setPassword(password);
		
		LocalSSHConnection connection = new LocalSSHConnection(accessor,
				settings);
		
		
		LinuxPromptPatternAgent agent = new LinuxPromptPatternAgent(connection);
		agent.execute("uname -l").readInput();
		
		
		connection.close();
	}
	
}