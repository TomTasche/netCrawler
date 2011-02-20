package at.andiwand.library.test;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import at.andiwand.library.network.ssh.SSH1Executor;



public class TestSSH1Executor {
	
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
		
		SSH1Executor sshExecutor = new SSH1Executor(login, password);
		
		System.out.println(sshExecutor.execute("ls /"));
		System.out.println(sshExecutor.lastExitStatus());
		System.out.println(sshExecutor.execute("ls /home"));
		System.out.println(sshExecutor.lastExitStatus());
	}
	
}