package at.netcrawler.ui.graphical.main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import at.andiwand.library.cli.CommandLineInterface;
import at.andiwand.library.component.CloseableTabbedPane;
import at.andiwand.library.io.FluidInputStreamReader;
import at.andiwand.library.io.StreamUtil;
import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.assistant.Configuration;
import at.netcrawler.assistant.ConfigurationDialog;
import at.netcrawler.assistant.ConnectionType;
import at.netcrawler.assistant.Encryption;
import at.netcrawler.assistant.EncryptionBag;
import at.netcrawler.assistant.EncryptionCallback;
import at.netcrawler.io.AfterLineMatchReader;
import at.netcrawler.io.FilterFirstLineReader;
import at.netcrawler.io.FilterLastLineReader;
import at.netcrawler.io.UntilLineMatchReader;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.LocalSSHConnection;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;
import at.netcrawler.network.connection.telnet.LocalTelnetConnection;
import at.netcrawler.network.connection.telnet.TelnetSettings;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.topology.TopologyDevice;


public class BatchManager extends JFrame {
	
	private static final long serialVersionUID = 4174046294656269705L;
	
	private static final String TITLE = "BatchMan";
	
	private JTextField addressField = new JTextField();
	private JComboBox connectionsCombo = new JComboBox(ConnectionType.values());
	private JTextField portField = new JTextField();
	private JTextField usernameField = new JTextField();
	private JPasswordField passwordField = new JPasswordField();
	private JTextField batchNewNameField = new JTextField();
	private CloseableTabbedPane batchTabbedPane = new CloseableTabbedPane();
	
	private JFileChooser batchFileChooser = new JFileChooser();
	private JFileChooser fileChooser = new JFileChooser();
	private File activeFile;
	
	private EncryptionBag encryptionBag;
	
	@SuppressWarnings("unchecked")
	public BatchManager(TopologyDevice device) {
		this();
		
		NetworkDevice networkDevice = device.getNetworkDevice();
		Set<String> addresses = (Set<String>) networkDevice.getValue(NetworkDevice.MANAGEMENT_ADDRESSES);
		if (addresses != null) {
			String address = addresses.iterator().next();
			addressField.setText(address);
		}
		
		// TODO:
		// connectionsCombo.setSelectedIndex();
		// portField.setText();
		// usernameField.setText();
		// passwordField.setText();
	}
	
	public BatchManager() {
		setTitle(TITLE);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
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
		JButton execute = new JButton("Execute");
		
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
							.addComponent(addressField)
							.addComponent(connectionsCombo)
							.addComponent(portField)
							.addComponent(usernameField)
							.addComponent(passwordField)
							.addGroup(layout.createSequentialGroup()
									.addComponent(batchNewNameField)
									.addComponent(batchAdd)
							)
					)
				)
				.addComponent(batchTabbedPane)
				.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
						.addComponent(execute)
						.addComponent(choose)
				)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(ipLabel)
						.addComponent(addressField)
				)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(connectionLabel)
						.addComponent(connectionsCombo)
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
						.addComponent(batchNewNameField)
						.addComponent(batchAdd)
				)
				.addComponent(batchTabbedPane)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(execute)
						.addComponent(choose)
				)
		);
		//@formatter:on
		
		connectionsCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectionType connection = (ConnectionType) connectionsCombo
						.getSelectedItem();
				
				portField.setText("" + connection.getDefaultPort());
			}
		});
		
		batchAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAddBatch();
			}
		});
		
		execute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					doExecute();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
				BatchManager.this.dispose();
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
			new IPv4Address(addressField.getText());
		} catch (Exception e) {
			throw new IllegalArgumentException("Illegal IP address!");
		}
	}
	
	private void validateConnection() {
		if (((ConnectionType) connectionsCombo.getSelectedItem()).legalConnection())
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
	
	private void doAddBatch() {
		String batchName = batchNewNameField.getText();
		batchName = batchName.trim();
		
		if (batchName.isEmpty()) {
			ConfigurationDialog.showErrorDialog(BatchManager.this,
					"Batch name is empty!");
			
			return;
		}
		
		for (int i = 0; i < batchTabbedPane.getTabCount(); i++) {
			if (batchName.equals(batchTabbedPane.getTitleAt(i))) {
				ConfigurationDialog.showErrorDialog(BatchManager.this,
						"Batch name already exists!");
				
				return;
			}
		}
		
		addBatch(batchName, "");
		batchTabbedPane.setSelectedIndex(batchTabbedPane.getTabCount() - 1);
		batchNewNameField.setText("");
	}
	
	private void doChoose() {
		if (batchTabbedPane.getTabCount() <= 0) {
			ConfigurationDialog.showErrorDialog(BatchManager.this,
					"Add batch name first!");
			return;
		}
		
		if (batchFileChooser.showOpenDialog(BatchManager.this) == JFileChooser.CANCEL_OPTION)
			return;
		
		File file = batchFileChooser.getSelectedFile();
		
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			
			StringBuilder builder = new StringBuilder();
			
			int tmp;
			while ((tmp = reader.read()) != -1) {
				builder.append((char) tmp);
			}
			
			((JTextArea) ((JScrollPane) batchTabbedPane.getSelectedComponent())
					.getViewport().getView()).setText(builder.toString());
		} catch (Exception e) {
			e.printStackTrace();
			ConfigurationDialog.showErrorDialog(BatchManager.this, e);
		}
	}
	
	private void doOpen() {
		if (fileChooser.showOpenDialog(this) == JFileChooser.CANCEL_OPTION)
			return;
		
		try {
			File file = fileChooser.getSelectedFile();
			final EncryptionBag encryptionBag = new EncryptionBag();
			encryptionBag.setEncryption(Encryption.PLAIN);
			
			Configuration configuration = new Configuration();
			configuration.readFromJsonFile(file, new EncryptionCallback() {
				public String getPassword(Encryption encryption) {
					String password = ConfigurationDialog
							.showDecryptionDialog(BatchManager.this);
					encryptionBag.setEncryption(encryption);
					encryptionBag.setPassword(password);
					return password;
				}
			});
			
			setConfiguration(configuration);
			
			this.encryptionBag = encryptionBag;
			activeFile = file;
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
			if (fileChooser.showSaveDialog(this) == JFileChooser.CANCEL_OPTION)
				return;
			
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
			configuration.writeToJsonFile(activeFile, encryptionBag
					.getEncryption(), encryptionBag.getPassword());
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
	
	private void doExecute() throws IOException {
		CommandLineInterface cli;
		
		IPv4Address ipAddress = new IPv4Address(addressField.getText());
		IPDeviceAccessor accessor = new IPDeviceAccessor(ipAddress);

		String batch = ((JTextField) ((JScrollPane) batchTabbedPane.getSelectedComponent()).getViewport().getView()).getText();;
		int port = Integer.parseInt(portField.getText());
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());
		if (connectionsCombo.getSelectedIndex() == 1 || connectionsCombo.getSelectedIndex() == 2) {
			SSHSettings settings = new SSHSettings();
			settings.setVersion(SSHVersion.VERSION2);
			settings.setVersion(connectionsCombo.getSelectedIndex() == 1 ? SSHVersion.VERSION1
					: SSHVersion.VERSION2);
			settings.setPort(port);
			settings.setUsername(username);
			settings.setPassword(password);
			
			LocalSSHConnection sshConsoleConnection = new LocalSSHConnection(
					accessor, settings);
			cli = sshConsoleConnection;
		} else if (connectionsCombo.getSelectedIndex() == 0) {
			TelnetSettings settings = new TelnetSettings();
			settings.setPort(port);
			
			LocalTelnetConnection telnetConnection = new LocalTelnetConnection(
					accessor, settings);
			cli = telnetConnection;
			
			OutputStream outputStream = cli.getOutputStream();
			
			if (!username.isEmpty()) {
				outputStream.write(username.getBytes());
				outputStream.write("\n".getBytes());
				outputStream.write(password.getBytes());
				outputStream.write("\n".getBytes());
			} else if (!password.isEmpty()) {
				outputStream.write(password.getBytes());
				outputStream.write("\n".getBytes());
			}
		} else {
			throw new IllegalStateException();
		}
		
		InputStream inputStream = cli.getInputStream();
		OutputStream outputStream = cli.getOutputStream();
		
		String start = "!-start-";
		Pattern startPattern = Pattern.compile(".+" + Pattern.quote(start));
		
		String end = "!-end-";
		Pattern endPattern = Pattern.compile(".+" + Pattern.quote(end));
		
		outputStream.write((start + "\n" + batch + "\n" + end + "\n")
				.getBytes());
		outputStream.flush();
		
		Reader reader = new FluidInputStreamReader(inputStream);
		reader = new AfterLineMatchReader(reader, startPattern);
		reader = new UntilLineMatchReader(reader, endPattern);
		reader = new FilterFirstLineReader(reader);
		reader = new FilterLastLineReader(reader);
		
		String result = StreamUtil.read(reader);
		
		cli.close();
		
		System.out.println(result);
	}
	
	private Configuration getConfiguration() {
		Configuration configuration = new Configuration();
		
		configuration.setAddress(new IPv4Address(addressField.getText()));
		configuration.setConnection((ConnectionType) connectionsCombo
				.getSelectedItem());
		configuration.setPort(Integer.parseInt(portField.getText()));
		configuration.setUsername(usernameField.getText());
		configuration.setPassword(new String(passwordField.getPassword()));
		
		for (int i = 0; i < batchTabbedPane.getTabCount(); i++) {
			configuration.putBatch(batchTabbedPane.getTitleAt(i),
					((JTextArea) ((JScrollPane) batchTabbedPane
							.getComponentAt(i)).getViewport().getView())
							.getText());
		}
		
		return configuration;
	}
	
	private void setConfiguration(Configuration configuration) {
		addressField.setText(configuration.getAddress().toString());
		connectionsCombo.setSelectedItem(configuration.getConnection());
		portField.setText("" + configuration.getPort());
		usernameField.setText(configuration.getUsername());
		passwordField.setText(configuration.getPassword());
		
		batchTabbedPane.removeAll();
		for (Map.Entry<String, String> entry : configuration.getBatches()
				.entrySet()) {
			addBatch(entry.getKey(), entry.getValue());
		}
	}
}