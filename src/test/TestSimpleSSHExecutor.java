package test;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import network.ssh.SimpleSSHExecutor;


public class TestSimpleSSHExecutor {
	
	public static String requestLogin(String message) {
		return JOptionPane.showInputDialog(message, "cisco@192.168.0.254");
	}
	
	public static String requestPassword(String message) {
		JPasswordField passwordField = new JPasswordField("cisco");
		int result = JOptionPane.showConfirmDialog(null, new Object[] {passwordField}, message, JOptionPane.OK_CANCEL_OPTION);
		
		if (result == JOptionPane.CANCEL_OPTION) return null;
		
		return new String(passwordField.getPassword());
	}
	
	
	public static void main(String[] args) throws Throwable {
		String login = requestLogin("Enter username@hostname");
		String password = requestPassword("Your password");
		
		SimpleSSHExecutor sshExecutor = new SimpleSSHExecutor(login, password);
		
		System.out.println(sshExecutor.execute("show cdp neighbors detail"));
		System.out.println(sshExecutor.getLastExitStatus());
	}
	
}