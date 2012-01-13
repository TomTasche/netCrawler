package at.netcrawler.assistant;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import javax.swing.filechooser.FileFilter;

import at.andiwand.library.component.CloseableTabbedPane;
import at.andiwand.library.component.JFrameUtil;
import at.andiwand.library.network.ip.IPv4Address;


public class ConfigurationManager extends JFrame {
	
	private static final long serialVersionUID = 4174046294656269705L;
	
	private static final String TITLE = "Configuration Manager";
	
	private JTextField address = new JTextField();
	private JComboBox connections = new JComboBox(Connection.values());
	private JTextField port = new JTextField();
	private JTextField username = new JTextField();
	private JPasswordField password = new JPasswordField();
	private JTextField batchNewName = new JTextField();
	private CloseableTabbedPane batchTabbedPane = new CloseableTabbedPane();
	
	private JFileChooser batchFileChooser = new JFileChooser();
	private JFileChooser fileChooser = new JFileChooser();
	private File activeFile;
	
	private EncryptionBag encryptionBag;
	
	public ConfigurationManager() {
		setTitle(TITLE);
		
		batchTabbedPane.setPreferredSize(new Dimension(300, 200));
		
		fileChooser.setFileFilter(new FileFilter() {
			public String getDescription() {
				return "Configuration file (*" + Configuration.FILE_SUFFIX
						+ ")";
			}
			
			public boolean accept(File f) {
				return f.isDirectory()
						|| f.getName().endsWith(Configuration.FILE_SUFFIX);
			}
		});
		
		JPanel panel = new JPanel();
		JButton batchAdd = new JButton("Add");
		JButton choose = new JButton("Choose batch");
		
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		JLabel ipLabel = new JLabel("Address:");
		JLabel connectionLabel = new JLabel("Connection:");
		JLabel portLabel = new JLabel("Port:");
		JLabel usernameLabel = new JLabel("Username:");
		JLabel passwordLabel = new JLabel("Password:");
		JLabel batchLabel = new JLabel("Batch:");
		
		//@formatter:off
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
							.addComponent(address)
							.addComponent(connections)
							.addComponent(port)
							.addComponent(username)
							.addComponent(password)
							.addGroup(layout.createSequentialGroup()
									.addComponent(batchNewName)
									.addComponent(batchAdd)
							)
					)
				)
				.addComponent(batchTabbedPane)
				.addComponent(choose, Alignment.TRAILING)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(ipLabel)
						.addComponent(address)
				)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(connectionLabel)
						.addComponent(connections)
				)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(portLabel)
						.addComponent(port)
				)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(usernameLabel)
						.addComponent(username)
				)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordLabel)
						.addComponent(password)
				)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(batchLabel)
						.addComponent(batchNewName)
						.addComponent(batchAdd)
				)
				.addComponent(batchTabbedPane)
				.addComponent(choose)
		);
		//@formatter:on
		
		connections.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Connection connection = (Connection) connections.getSelectedItem();
				
				port.setText("" + connection.getDefaultPort());
			}
		});
		
		batchAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAddBatch();
			}
		});
		
		choose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				doChoose();
			}
		});
		
		add(panel);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem open = new JMenuItem("Open...");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem saveAs = new JMenuItem("Save as...");
		JMenuItem exit = new JMenuItem("Exit");
		
		file.add(open);
		file.addSeparator();
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
		setMinimumSize(getSize());
	}
	
	private void addBatch(String name, String batch) {
		JTextArea textArea = new JTextArea(batch);
		JScrollPane scrollPane = new JScrollPane(textArea);
		batchTabbedPane.add(name, scrollPane);
	}
	
	private void validateIP() {
		try {
			IPv4Address.getByAddress(address.getText());
		} catch (Exception e) {
			throw new IllegalArgumentException("Illegal IP address!");
		}
	}
	
	private void validateConnection() {
		if (((Connection) connections.getSelectedItem()).legalConnection()) return;
		
		throw new IllegalArgumentException("Choose connection!");
	}
	
	private void validatePort() {
		try {
			Integer.parseInt(port.getText());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Illegal port!");
		}
	}
	
	private void validateBatch() {
		if (batchTabbedPane.getTabCount() <= 0) throw new IllegalArgumentException(
				"Contains no batches!");
	}
	
	private void validateAll() {
		validateIP();
		validateConnection();
		validatePort();
		validateBatch();
	}
	
	private void doAddBatch() {
		String batchName = batchNewName.getText();
		batchName = batchName.trim();
		
		if (batchName.isEmpty()) {
			ConfigurationDialog.showErrorDialog(ConfigurationManager.this,
					"Batch name is empty!");
			
			return;
		}
		
		for (int i = 0; i < batchTabbedPane.getTabCount(); i++) {
			if (batchName.equals(batchTabbedPane.getTitleAt(i))) {
				ConfigurationDialog.showErrorDialog(ConfigurationManager.this,
						"Batch name already exists!");
				
				return;
			}
		}
		
		addBatch(batchName, "");
		batchTabbedPane.setSelectedIndex(batchTabbedPane.getTabCount() - 1);
		batchNewName.setText("");
	}
	
	private void doChoose() {
		if (batchTabbedPane.getTabCount() <= 0) {
			ConfigurationDialog.showErrorDialog(ConfigurationManager.this,
					"Add batch name first!");
			return;
		}
		
		if (batchFileChooser.showOpenDialog(ConfigurationManager.this) == JFileChooser.CANCEL_OPTION) return;
		
		File file = batchFileChooser.getSelectedFile();
		
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			
			StringBuilder builder = new StringBuilder();
			
			int tmp;
			while ((tmp = reader.read()) != -1) {
				builder.append((char) tmp);
			}
			
			((JTextArea) ((JScrollPane) batchTabbedPane.getSelectedComponent()).getViewport().getView()).setText(builder.toString());
		} catch (Exception e) {
			e.printStackTrace();
			ConfigurationDialog.showErrorDialog(ConfigurationManager.this, e);
		}
	}
	
	private void doOpen() {
		if (fileChooser.showOpenDialog(this) == JFileChooser.CANCEL_OPTION) return;
		
		try {
			final EncryptionBag encryptionBag = new EncryptionBag();
			encryptionBag.setEncryption(Encryption.PLAIN);
			
			Configuration configuration = new Configuration();
			configuration.readFromJsonFile(fileChooser.getSelectedFile(),
					new EncryptionCallback() {
						public String getPassword(Encryption encryption) {
							String password = ConfigurationDialog.showDecryptionDialog(ConfigurationManager.this);
							encryptionBag.setEncryption(encryption);
							encryptionBag.setPassword(password);
							return password;
						}
					});
			
			this.encryptionBag = encryptionBag;
			
			setConfiguration(configuration);
		} catch (IOException e) {
			e.printStackTrace();
			ConfigurationDialog.showErrorDialog(this, e);
		}
	}
	
	private void doSave() {
		try {
			validateAll();
		} catch (Exception e) {
			ConfigurationDialog.showErrorDialog(this, e);
			return;
		}
		
		if (activeFile == null) {
			if (fileChooser.showSaveDialog(this) == JFileChooser.CANCEL_OPTION) return;
			
			activeFile = fileChooser.getSelectedFile();
			
			if (!activeFile.getName().endsWith(Configuration.FILE_SUFFIX)) {
				activeFile = new File(activeFile.getPath()
						+ Configuration.FILE_SUFFIX);
			}
		}
		
		try {
			if (encryptionBag == null) {
				encryptionBag = ConfigurationDialog.showEncryptionDialog(this);
				
				if (encryptionBag == null) return;
			}
			
			Configuration configuration = getConfiguration();
			configuration.writeToJsonFile(activeFile,
					encryptionBag.getEncryption(), encryptionBag.getPassword());
		} catch (IOException e) {
			e.printStackTrace();
			ConfigurationDialog.showErrorDialog(this, e);
		}
	}
	
	private void doSaveAs() {
		File tmp = activeFile;
		activeFile = null;
		
		encryptionBag = null;
		
		doSave();
		
		if (activeFile == null) activeFile = tmp;
	}
	
	private Configuration getConfiguration() {
		Configuration configuration = new Configuration();
		
		configuration.setAddress(IPv4Address.getByAddress(address.getText()));
		configuration.setConnection((Connection) connections.getSelectedItem());
		configuration.setPort(Integer.parseInt(port.getText()));
		configuration.setUsername(username.getText());
		configuration.setPassword(new String(password.getPassword()));
		
		for (int i = 0; i < batchTabbedPane.getTabCount(); i++) {
			configuration.putBatch(
					batchTabbedPane.getTitleAt(i),
					((JTextArea) ((JScrollPane) batchTabbedPane.getComponentAt(i)).getViewport().getView()).getText());
		}
		
		return configuration;
	}
	
	private void setConfiguration(Configuration configuration) {
		address.setText(configuration.getAddress().toString());
		connections.setSelectedItem(configuration.getConnection());
		port.setText("" + configuration.getPort());
		username.setText(configuration.getUsername());
		password.setText(configuration.getPassword());
		
		batchTabbedPane.removeAll();
		for (Map.Entry<String, String> entry : configuration.getBatches().entrySet()) {
			addBatch(entry.getKey(), entry.getValue());
		}
	}
	
	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JOptionPane.setDefaultLocale(Locale.US);
		
		ConfigurationManager configurationManager = new ConfigurationManager();
		JFrameUtil.centerFrame(configurationManager);
		configurationManager.setDefaultCloseOperation(EXIT_ON_CLOSE);
		configurationManager.setVisible(true);
	}
	
}