package at.andiwand.library.test;

import java.io.InputStream;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import at.andiwand.library.network.ssh.SSH1Client;


public class TestSSH1Client {
	
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
		
		SSH1Client client = new SSH1Client(login, password);
		client.getOutputStream().write("\r\n".getBytes());
		
		InputStream inputStream = client.getInputStream();
		int read;
		while ((read = inputStream.read()) != -1) {
			System.out.write(read);
		}
	}
	
}