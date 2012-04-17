package at.netcrawler.ui.crawler;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.ui.DialogUtil;


public class CrawlDialog {
	
	private static final Insets DEFAULT_INSETS = new Insets(0, 5, 5, 5);
	
	public static CrawlSettings showCrawlDialog(Component parent) {
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		
		JLabel addressLabel = new JLabel("Choose the first device to crawl:");
		JLabel usernameLabel = new JLabel("Default username:");
		JLabel passwordLabel = new JLabel("Default password:");
		final JTextField addressField = new JTextField();
		JTextField usernameField = new JTextField();
		JPasswordField passwordField = new JPasswordField();
		JToggleButton advancedButton = new JToggleButton(
				"Show advanced settings");
		
		passwordField.setPreferredSize(new Dimension(150, passwordField
				.getPreferredSize().height));
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.weightx = 0.1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = DEFAULT_INSETS;
		panel.add(addressLabel, constraints);
		
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.weightx = 0.1;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.insets = DEFAULT_INSETS;
		panel.add(addressField, constraints);
		
		constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.weightx = 0.1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = DEFAULT_INSETS;
		panel.add(usernameLabel, constraints);
		
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.weightx = 0.1;
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.insets = DEFAULT_INSETS;
		panel.add(usernameField, constraints);
		
		constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.weightx = 0.1;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.insets = DEFAULT_INSETS;
		panel.add(passwordLabel, constraints);
		
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.weightx = 0.1;
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.insets = DEFAULT_INSETS;
		panel.add(passwordField, constraints);
		
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.weightx = 0.1;
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.insets = DEFAULT_INSETS;
		panel.add(advancedButton, constraints);
		
		JOptionPane optionPane = new JOptionPane(panel,
				JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION) {
			private static final long serialVersionUID = -3897964554672819739L;
			
			public void selectInitialValue() {
				addressField.requestFocusInWindow();
			}
		};
		
		while (true) {
			JDialog dialog = optionPane.createDialog(parent, "Crawl settings");
			dialog.pack();
			dialog.setVisible(true);
			dialog.dispose();
			
			if ((Integer) optionPane.getValue() != JOptionPane.OK_OPTION)
				return null;
			
			IPv4Address address;
			try {
				address = new IPv4Address(addressField.getText());
			} catch (Exception e) {
				DialogUtil
						.showErrorDialog(parent, "The passwords don't match!");
				
				continue;
			}
			
			String username = usernameField.getText();
			String password = new String(passwordField.getPassword());
			if (username.trim().isEmpty()) {
				DialogUtil.showErrorDialog(parent, "Password is empty!");
				
				continue;
			}
			if (password.trim().isEmpty()) {
				DialogUtil.showErrorDialog(parent, "Username is empty");
				
				continue;
			}
			
			CrawlSettings settings = new CrawlSettings();
			settings.setAddress(address);
			settings.setDefaultUsername(username);
			settings.setDefaultPassword(password);
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		CrawlDialog.showCrawlDialog(null);
	}
	
}