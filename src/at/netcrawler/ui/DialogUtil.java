package at.netcrawler.ui;

import java.awt.Component;

import javax.swing.JOptionPane;


public class DialogUtil {
	
	public static void showErrorDialog(Component parent, Throwable t) {
		showErrorDialog(parent, t.getMessage());
	}
	
	public static void showErrorDialog(Component parent, String message) {
		showErrorDialog(parent, "Error", message);
	}
	
	public static void showErrorDialog(Component parent, String title,
			String message) {
		JOptionPane.showMessageDialog(parent, message, title,
				JOptionPane.ERROR_MESSAGE);
	}
	
}