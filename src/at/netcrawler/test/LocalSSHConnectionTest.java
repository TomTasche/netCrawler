package at.netcrawler.test;

import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.LocalSSHConnection;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;


public class LocalSSHConnectionTest {
	
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
		
		LocalSSHConnection connection = new LocalSSHConnection(accessor,
				settings);
		
		InputStream inputStream = connection.getInputStream();
		OutputStream outputStream = connection.getOutputStream();
		
		outputStream.write("uname -a\r\n".getBytes());
		outputStream.flush();
		
		while (true) {
			System.out.write(inputStream.read());
		}
	}
	
}