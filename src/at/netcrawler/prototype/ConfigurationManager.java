package at.netcrawler.prototype;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import at.andiwand.library.component.CloseableTabbedPane;
import at.andiwand.library.network.ip.IPv4Address;


public class ConfigurationManager extends JFrame {
	
	private static final long serialVersionUID = 4174046294656269705L;
	
	private static class EncryptionBag {
		public Encryption encryption;
		public String password;
		
		public EncryptionBag() {}
		public EncryptionBag(Encryption encryption, String password) {
			this.encryption = encryption;
			this.password = password;
		}
	}
	
	private static final String TITLE = "Configuration Manager";
	
	
	private JPanel panel = new JPanel();
	
	private JTextField ipField = new JTextField();
	private JComboBox connectionBox = new JComboBox(Connection.values());
	private JTextField portField = new JTextField();
	private JTextField usernameField = new JTextField();
	private JPasswordField passwordField = new JPasswordField();
	private JTextField batchNewField = new JTextField();
	private JButton batchAddButton = new JButton("Add");
	private CloseableTabbedPane batchTabbedPane = new CloseableTabbedPane();
	private JButton fileButton = new JButton("Choose batch");
	
	private JFileChooser batchFileChooser = new JFileChooser();
	private JFileChooser fileChooser = new JFileChooser();
	
	private EncryptionBag actualEncryptionBag;
	

	public ConfigurationManager() {
		setTitle(TITLE);
		
		batchNewField.setPreferredSize(new Dimension(150, batchNewField
				.getPreferredSize().height));
		batchTabbedPane.setPreferredSize(new Dimension(300, 300));
		
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		JLabel ipLabel = new JLabel("IP:");
		JLabel connectionLabel = new JLabel("Connection:");
		JLabel portLabel = new JLabel("Port:");
		JLabel usernameLabel = new JLabel("Username:");
		JLabel passwordLabel = new JLabel("Password:");
		JLabel batchLabel  = new JLabel("Batch:");
		
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup()
							.addComponent(ipLabel)
							.addComponent(connectionLabel)
							.addComponent(portLabel)
							.addComponent(usernameLabel)
							.addComponent(passwordLabel)
							.addComponent(batchLabel)
					)
					.addGroup(layout.createParallelGroup()
							.addComponent(ipField)
							.addComponent(connectionBox)
							.addComponent(portField)
							.addComponent(usernameField)
							.addComponent(passwordField)
							.addGroup(layout.createSequentialGroup()
									.addComponent(batchNewField)
									.addComponent(batchAddButton)
							)
					)
				)
				.addComponent(batchTabbedPane)
				.addComponent(fileButton, Alignment.TRAILING)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(ipLabel)
						.addComponent(ipField)
				)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(connectionLabel)
						.addComponent(connectionBox)
				)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(portLabel)
						.addComponent(portField)
				)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(usernameLabel)
						.addComponent(usernameField)
				)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordLabel)
						.addComponent(passwordField)
				)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(batchLabel)
						.addComponent(batchNewField)
						.addComponent(batchAddButton)
				)
				.addComponent(batchTabbedPane)
				.addComponent(fileButton)
		);
		
		add(panel);

		fileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				batchFileChooser.showOpenDialog(ConfigurationManager.this);

				File batchFile = batchFileChooser.getSelectedFile();
				
				if (batchFile == null) {
					return;
				}
				
				try {
					FileReader fileReader = new FileReader(batchFile);
					BufferedReader reader = new BufferedReader(fileReader);
					
					StringBuilder builder = new StringBuilder();
					
					int tmp;
					while ((tmp = reader.read()) != -1) {
						builder.append((char) tmp);
					}
					
					((JTextArea) ((JScrollPane) batchTabbedPane
							.getSelectedComponent()).getViewport().getView())
							.setText(builder.toString());
				} catch (Exception e) {
					e.printStackTrace();
					showErrorDialog(e);
				}
			}
		});
		
		connectionBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Connection connection = (Connection) connectionBox
						.getSelectedItem();
				
				portField.setText("" + connection.getDefaultPort());
			}
		});
		
		batchAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String batchName = batchNewField.getText();
				batchName = batchName.trim();
				
				if (batchName.isEmpty()) {
					showErrorDialog("Batch name is empty!");
					
					return;
				}
				
				for (int i = 0; i < batchTabbedPane.getTabCount(); i++) {
					if (batchName.equals(batchTabbedPane.getTitleAt(i))) {
						showErrorDialog("Batch name already exists!");
						
						return;
					}
				}
				
				addBatch(batchName, "");
				batchTabbedPane.setSelectedIndex(batchTabbedPane.getTabCount() - 1);
				batchNewField.setText("");
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem open = new JMenuItem("Open...");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem saveAs = new JMenuItem("Save as...");
		JMenuItem exit = new JMenuItem("Exit");
		
		file.add(open);
		file.add(save);
		file.add(saveAs);
		file.addSeparator();
		file.add(exit);
		menuBar.add(file);
		
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				doOpen();
			}
		});
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				doSave();
			}
		});
		
		saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				doSaveAs();
			}
		});
		
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfigurationManager.this.dispose();
			}
		});
		
		setJMenuBar(menuBar);
		
		pack();
		centerFrame();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void centerFrame() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dimension.width - getWidth()) >> 1,
				(dimension.height - getHeight()) >> 1);
	}
	
	private void addBatch(String name, String batch) {
		JTextArea textArea = new JTextArea(batch);
		JScrollPane scrollPane = new JScrollPane(textArea);
		batchTabbedPane.add(name, scrollPane);
	}
	
	private void validateIP() {
		try {
			IPv4Address.getByAddress(ipField.getText());
		} catch (Exception e) {
			throw new IllegalArgumentException("Illegal ip Address!");
		}
	}
	private void validateConnection() {
		if (((Connection) connectionBox.getSelectedItem()).legalConnection())
			return;
		
		throw new IllegalArgumentException("Choose connection!");
	}
	private void validatePort() {
		try {
			Integer.parseInt(portField.getText());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Illegal port!");
		}
	}
	private void validateBatch() {
		if (batchTabbedPane.getTabCount() <= 0)
			throw new IllegalArgumentException("Contains no batches!");
	}
	private void validateAll() {
		validateIP();
		validateConnection();
		validatePort();
		validateBatch();
	}
	private void validateAllAndShowError() {
		try {
			validateAll();
		} catch (Exception e) {
			showErrorDialog(e);
		}
	}
	
	private void doOpen() {
		if (fileChooser.showOpenDialog(this) == JFileChooser.CANCEL_OPTION)
			return;
		
		try {
			final EncryptionBag encryptionBag = new EncryptionBag();
			encryptionBag.encryption = Encryption.PLAIN;
			
			Configuration configuration = new Configuration();
			configuration.readFromJsonFile(fileChooser.getSelectedFile(),
					new EncryptionCallback() {
						public String getPassword(Encryption encryption) {
							String password = showDecryptionDialog();
							encryptionBag.encryption = encryption;
							encryptionBag.password = password;
							return password;
						}
					});
			
			actualEncryptionBag = encryptionBag;
			
			setConfiguration(configuration);
		} catch (Exception e) {
			e.printStackTrace();
			showErrorDialog(e);
		}
	}
	private void doSave() {
		if (fileChooser.getSelectedFile() == null) {
			doSaveAs();
			return;
		} else {
			validateAllAndShowError();
		}
		
		try {
			if (actualEncryptionBag == null) {
				actualEncryptionBag = showEncryptionDialog();
				
				if (actualEncryptionBag == null)
					return;
			}
			
			Configuration configuration = getConfiguration();
			configuration.writeToJsonFile(fileChooser.getSelectedFile(),
					actualEncryptionBag.encryption,
					actualEncryptionBag.password);
		} catch (Exception e) {
			e.printStackTrace();
			showErrorDialog(e);
		}
	}
	private void doSaveAs() {
		validateAllAndShowError();
		
		if (fileChooser.showSaveDialog(this) == JFileChooser.CANCEL_OPTION)
			return;
		
		doSave();
	}
	
	private Configuration getConfiguration() {
		Configuration configuration = new Configuration();
		
		configuration.address = IPv4Address.getByAddress(ipField.getText());
		configuration.connection = (Connection) connectionBox.getSelectedItem();
		configuration.port = Integer.parseInt(portField.getText());
		configuration.username = usernameField.getText();
		configuration.password = new String(passwordField.getPassword());
		
		for (int i = 0; i < batchTabbedPane.getTabCount(); i++) {
			configuration.batchMap.put(batchTabbedPane.getTitleAt(i),
					((JTextArea) ((JScrollPane) batchTabbedPane
							.getComponentAt(i)).getViewport().getView())
							.getText());
		}
		
		return configuration;
	}
	
	private void setConfiguration(Configuration configuration) {
		ipField.setText(configuration.address.toString());
		connectionBox.setSelectedItem(configuration.connection);
		portField.setText("" + configuration.port);
		usernameField.setText(configuration.username);
		passwordField.setText(configuration.password);
		
		batchTabbedPane.removeAll();
		for (Map.Entry<String, String> entry : configuration.batchMap
				.entrySet()) {
			addBatch(entry.getKey(), entry.getValue());
		}
	}
	
	private void showErrorDialog(Throwable t) {
		showErrorDialog(t.getMessage());
	}
	private void showErrorDialog(String message) {
		showErrorDialog("Error", message);
	}
	private void showErrorDialog(String title, String message) {
		JOptionPane.showMessageDialog(this, message, title,
				JOptionPane.ERROR_MESSAGE);
	}
	
	private EncryptionBag showEncryptionDialog() {
		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		JLabel label = new JLabel("Choose your encrytion settings:");
		JLabel encryptionLabel = new JLabel("Method:");
		JLabel passwordLabel = new JLabel("Password:");
		JLabel passwordRepeatLabel = new JLabel("Repeat Password:");
		final JComboBox encryptionComboBox = new JComboBox(Encryption.values());
		final JPasswordField passwordField = new JPasswordField();
		final JPasswordField passwordRepeatField = new JPasswordField();
		
		encryptionComboBox.setSelectedItem(Encryption.DES);
		passwordField.setPreferredSize(new Dimension(150, passwordField
				.getPreferredSize().height));
		
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
		
		while (true) {
			if (JOptionPane.showConfirmDialog(this, panel, "Encryption Settings",
					JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
				return null;
			
			if (Arrays.equals(passwordField.getPassword(),
					passwordRepeatField.getPassword()))
				break;
			
			showErrorDialog("The passwords don't match!");
			passwordField.setText("");
			passwordRepeatField.setText("");
		}
		
		return new EncryptionBag(
				(Encryption) encryptionComboBox.getSelectedItem(), new String(
						passwordField.getPassword()));
	}
	
	private String showDecryptionDialog() {
		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		JLabel label = new JLabel("Choose your decrytion settings:");
		JLabel passwordLabel = new JLabel("Password:");
		JPasswordField passwordField = new JPasswordField();
		
		passwordField.requestFocus();
		passwordField.setPreferredSize(new Dimension(150, passwordField
				.getPreferredSize().height));
		
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
		
		if (JOptionPane.showConfirmDialog(this, panel, "Decryption Settings",
				JOptionPane.OK_OPTION) != JOptionPane.OK_OPTION)
			return "";
		
		return new String(passwordField.getPassword());
	}
	
	
	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JOptionPane.setDefaultLocale(Locale.US);
		
		new ConfigurationManager();
	}
	
}
