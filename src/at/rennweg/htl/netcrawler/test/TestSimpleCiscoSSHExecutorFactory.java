package at.rennweg.htl.netcrawler.test;

import java.net.InetAddress;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;
import at.rennweg.htl.netcrawler.cli.executor.SimpleRemoteExecutor;
import at.rennweg.htl.netcrawler.cli.executor.factory.SimpleCiscoRemoteExecutorFactory;
import at.rennweg.htl.netcrawler.cli.executor.factory.SimpleCiscoSSHExecutorFactory;


public class TestSimpleCiscoSSHExecutorFactory {
	
	public static String requestLogin(String message) {
		return JOptionPane.showInputDialog(message, "cisco@192.168.0.254");
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
		
		InetAddress address = InetAddress.getByName(login.split("@")[1]);
		SimpleCiscoUser user = new SimpleCiscoUser(login.split("@")[0], password);
		
		SimpleCiscoRemoteExecutorFactory executorFactory = new SimpleCiscoSSHExecutorFactory();
		SimpleRemoteExecutor remoteExecutor = executorFactory.getRemoteExecutor(address, user);
		
		System.out.println(remoteExecutor.execute("show version"));
		System.out.println(remoteExecutor.execute("show running-config"));
	}
	
}