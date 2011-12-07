package at.netcrawler.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import at.andiwand.library.cli.CommandLine;
import at.andiwand.library.io.TeeInputStream;
import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.cli.agent.LinuxCommandLineAgent;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.LocalSSHConnection;
import at.netcrawler.network.connection.ssh.SSHSettings;
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
		
		SSHSettings settings = new SSHSettings();
		settings.setVersion(version);
		settings.setPort(port);
		settings.setUsername(username);
		settings.setPassword(password);
		
		final LocalSSHConnection connection = new LocalSSHConnection();
		connection.connect(accessor, settings);
		
		LinuxCommandLineAgent agent = new LinuxCommandLineAgent(
				new CommandLine() {
					public OutputStream getOutputStream() throws IOException {
						return connection.getOutputStream();
					}
					
					public InputStream getInputStream() throws IOException {
						return new TeeInputStream(connection.getInputStream(),
								System.out);
					}
					
					public void close() throws IOException {
						connection.close();
					}
				});
		agent.execute("uname -a");
		
		connection.close();
	}
	
}