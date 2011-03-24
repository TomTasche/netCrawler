package at.andiwand.library.test;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import at.andiwand.library.network.ssh.SSH2Client;
import at.andiwand.library.util.cli.LinuxBashExecutor;


public class TestLinuxBashExecutor {
	
	public static String requestLogin(String message) {
		return JOptionPane.showInputDialog(message, System.getProperty("user.name") + "@localhost");
	}
	
	public static String requestPassword(String message) {
		JPasswordField passwordField = new JPasswordField();
		int result = JOptionPane.showConfirmDialog(null, new Object[] {passwordField}, message, JOptionPane.OK_CANCEL_OPTION);
		
		if (result == JOptionPane.CANCEL_OPTION) return null;
		
		return new String(passwordField.getPassword());
	}
	
	
	public static void main(String[] args) throws Throwable {
		String login = requestLogin("Enter username@hostname");
		String password = requestPassword("Your password");
		
		SSH2Client sshClient = new SSH2Client(login, password);
		LinuxBashExecutor bashExecutor = new LinuxBashExecutor(sshClient);
		
		String[] commands = {"ps", "w"};
		
		for (String command : commands) {
			Process process = bashExecutor.execute(command);
			
			int read;
			while ((read = process.getInputStream().read()) != -1) {
				System.out.print((char) read);
			}
			
			System.out.println();
			System.out.println();
		}
	}
	
}