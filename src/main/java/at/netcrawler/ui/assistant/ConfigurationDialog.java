package at.netcrawler.ui.assistant;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import at.netcrawler.util.DialogUtil;


public class ConfigurationDialog {
	
	public static EncryptionBag showEncryptionDialog(Component parent) {
		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		JLabel label = new JLabel("Choose your encryption settings:");
		JLabel encryptionLabel = new JLabel("Method:");
		JLabel passwordLabel = new JLabel("Password:");
		JLabel passwordRepeatLabel = new JLabel("Repeat Password:");
		final JComboBox encryptionComboBox = new JComboBox(Encryption.values());
		final JPasswordField passwordField = new JPasswordField();
		final JPasswordField passwordRepeatField = new JPasswordField();
		
		encryptionComboBox.setSelectedItem(Encryption.DES);
		passwordField.setPreferredSize(new Dimension(150, passwordField
				.getPreferredSize().height));
		
		//@formatter:off
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(label)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addComponent(encryptionLabel)
								.addComponent(passwordLabel)
								.addComponent(passwordRepeatLabel)
						)
						.addGroup(layout.createParallelGroup()
								.addComponent(encryptionComboBox)
								.addComponent(passwordField)
								.addComponent(passwordRepeatField)
						)
				)
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(label)
				.addGap(20)
				.addGroup(layout.createParallelGroup()
						.addComponent(encryptionLabel)
						.addComponent(encryptionComboBox)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(passwordLabel)
						.addComponent(passwordField)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(passwordRepeatLabel)
						.addComponent(passwordRepeatField)
				)
		);
		//@formatter:on
		
		encryptionComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (encryptionComboBox.getSelectedItem() == Encryption.PLAIN) {
					passwordField.setEnabled(false);
					passwordRepeatField.setEnabled(false);
				} else {
					passwordField.setEnabled(true);
					passwordRepeatField.setEnabled(true);
				}
			}
		});
		
		JOptionPane optionPane = new JOptionPane(panel,
				JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION) {
			private static final long serialVersionUID = 646321718244222228L;
			
			public void selectInitialValue() {
				passwordField.requestFocusInWindow();
			}
		};
		
		while (true) {
			JDialog dialog = optionPane.createDialog(parent,
					"Encryption settings");
			dialog.setVisible(true);
			dialog.dispose();
			
			if ((Integer) optionPane.getValue() != JOptionPane.OK_OPTION)
				return null;
			
			if (Arrays.equals(passwordField.getPassword(), passwordRepeatField
					.getPassword())) break;
			
			DialogUtil.showErrorDialog(parent, "The passwords don't match!");
			passwordField.setText("");
			passwordRepeatField.setText("");
		}
		
		return new EncryptionBag((Encryption) encryptionComboBox
				.getSelectedItem(), new String(passwordField.getPassword()));
	}
	
	public static String showDecryptionDialog(Component parent) {
		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		JLabel label = new JLabel("Choose your decryption password:");
		JLabel passwordLabel = new JLabel("Password:");
		final JPasswordField passwordField = new JPasswordField();
		
		passwordField.setPreferredSize(new Dimension(150, passwordField
				.getPreferredSize().height));
		
		//@formatter:off
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(label)
				.addGroup(layout.createSequentialGroup()
						.addComponent(passwordLabel)
						.addComponent(passwordField)
				)
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(label)
				.addGap(20)
				.addGroup(layout.createParallelGroup()
						.addComponent(passwordLabel)
						.addComponent(passwordField)
				)
		);
		//@formatter:on
		
		JOptionPane optionPane = new JOptionPane(panel,
				JOptionPane.QUESTION_MESSAGE) {
			private static final long serialVersionUID = 646321718244222228L;
			
			public void selectInitialValue() {
				passwordField.requestFocusInWindow();
			}
		};
		
		JDialog dialog = optionPane.createDialog(parent, "Decryption password");
		dialog.setVisible(true);
		dialog.dispose();
		
		return new String(passwordField.getPassword());
	}
}