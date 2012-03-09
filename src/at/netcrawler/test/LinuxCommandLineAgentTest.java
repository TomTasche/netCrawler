package at.netcrawler.test;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.cli.agent.LinuxCommandLineAgent;
import at.netcrawler.cli.agent.LinuxCLIAgentSettings;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.LocalSSHConnection;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;


public class LinuxCommandLineAgentTest {
	
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
		SSHVersion version = SSHVersion.VERSION2;
		String address = "127.0.0.1";
		IPv4Address ipAddress = new IPv4Address(address);
		int port = 22;
		String username = "andreas";
		String password = getPassword(username);
		
		IPDeviceAccessor accessor = new IPDeviceAccessor(ipAddress);
		
		SSHSettings settings = new SSHSettings();
		settings.setVersion(version);
		settings.setPort(port);
		settings.setUsername(username);
		settings.setPassword(password);
		
		final LocalSSHConnection connection = new LocalSSHConnection(accessor,
				settings);
		
		LinuxCLIAgentSettings agentSettings = new LinuxCLIAgentSettings();
		
		LinuxCommandLineAgent agent = new LinuxCommandLineAgent(connection,
				agentSettings);
		System.out.println(agent.executeAndRead("uname -a"));
		System.out.println(agent.executeAndRead("pwd"));
		System.out.println(agent.executeAndRead("date"));
		
		connection.close();
	}
	
}