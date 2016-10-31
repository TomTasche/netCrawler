package at.netcrawler.ui.assistant;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Iterator;
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
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle;
import javax.swing.filechooser.FileFilter;

import at.andiwand.library.cli.CommandLineInterface;
import at.andiwand.library.io.StreamUtil;
import at.netcrawler.io.UntilLineMatchInputStream;
import at.netcrawler.network.accessor.DeviceAccessor;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ConnectionBuilder;
import at.netcrawler.network.connection.ConnectionSettings;
import at.netcrawler.network.connection.ssh.LocalSSHGateway;
import at.netcrawler.network.connection.telnet.LocalTelnetGateway;
import at.netcrawler.util.DialogUtil;


public class ConfigurationExecutor extends JFrame {
	
	private static final long serialVersionUID = 7767764252271031663L;
	
	private static final String TITLE = "Configuration Executor";
	
	// TODO: other os?
	private static final String BATCH_SUFFIX = "!executorEnd";
	private static final Pattern BATCH_END_PATTERN = Pattern.compile(".+"
			+ BATCH_SUFFIX);
	
	private JLabel address = new JLabel();
	private JLabel connection = new JLabel();
	private JLabel port = new JLabel();
	private JComboBox batches = new JComboBox();
	private JButton execute = new JButton("Execute");
	private JToggleButton resultButton = new JToggleButton("Show result");
	private JTabbedPane resultPane = new JTabbedPane();
	private JLabel status = new JLabel();
	
	private JFileChooser fileChooser = new JFileChooser();
	
	private Configuration configuration;
	
	private ConnectionBuilder connectionFactory = new ConnectionBuilder(
			new LocalTelnetGateway(), new LocalSSHGateway());
	
	public ConfigurationExecutor(Configuration configuration) {
		this();
		
		setEnabledAll(true);
		
		setConfiguration(configuration);
		
		pack();
		setMinimumSize(getSize());
	}
	
	public ConfigurationExecutor() {
		setTitle(TITLE);
		
		JPanel panel = new JPanel();
		JLabel addressLabel = new JLabel("Address:");
		JLabel connectionLabel = new JLabel("Connection:");
		JLabel portLabel = new JLabel("Port:");
		JLabel batchLabel = new JLabel("Batch:");
		JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		
		resultPane.setVisible(false);
		
		batches.setPreferredSize(new Dimension(150,
				batches.getPreferredSize().height));
		setEnabledAll(false);
		
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
		
		GroupLayout layout = new GroupLayout(panel);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		panel.setLayout(layout);
		
		//@formatter:off
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addComponent(addressLabel)
								.addComponent(connectionLabel)
								.addComponent(portLabel)
								.addComponent(batchLabel)
								)
						.addGap(20)
						.addGroup(layout.createParallelGroup()
								.addComponent(address)
								.addComponent(connection)
								.addComponent(port)
								.addComponent(batches)
								)
				)
				.addComponent(separator)
				.addComponent(status, Alignment.LEADING)
				.addGroup(Alignment.TRAILING, layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
							.addComponent(resultButton)
							.addComponent(execute)
							)
						)
				.addComponent(resultPane, Alignment.TRAILING)
		);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(addressLabel)
						.addComponent(address)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(connectionLabel)
						.addComponent(connection)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(portLabel)
						.addComponent(port)
						)
				.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(batchLabel)
						.addComponent(batches)
						)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
						20, Short.MAX_VALUE)
				.addComponent(separator, GroupLayout.PREFERRED_SIZE,
						GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGap(10)
				.addGroup(layout.createParallelGroup()
						.addComponent(status)
						.addComponent(resultButton)
						.addComponent(execute)
						)
				.addComponent(resultPane)
		);
		//@formatter:on
		
		execute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doExecute();
			}
		});
		
		add(panel);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem open = new JMenuItem("Open...");
		JMenuItem exit = new JMenuItem("Exit");
		
		menuBar.add(file);
		file.add(open);
		file.addSeparator();
		file.add(exit);
		
		resultButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultPane.setVisible(resultButton.isSelected());
				
				validate();
				pack();
			}
		});
		
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doOpen();
			}
		});
		
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfigurationExecutor.this.dispose();
			}
		});
		
		setJMenuBar(menuBar);
		
		pack();
		setMinimumSize(getSize());
	}
	
	private void setEnabledAll(boolean enabled) {
		batches.setEnabled(enabled);
		execute.setEnabled(enabled);
	}
	
	private void doOpen() {
		if (fileChooser.showOpenDialog(this) == JFileChooser.CANCEL_OPTION)
			return;
		
		try {
			open(fileChooser.getSelectedFile());
			
			setEnabledAll(true);
		} catch (IOException e) {
			e.printStackTrace();
			DialogUtil.showErrorDialog(this, e);
		}
	}
	
	public void doExecute() {
		new Thread() {
			@Override
			public void run() {
				status.setText("Executing...");
				
				try {
					resultPane.removeAll();
					
					Set<InetAddress> addresses = configuration.getAddresses();
					for (InetAddress address : addresses) {
						JTextArea result = new JTextArea();
						JScrollPane resultScroll = new JScrollPane(result);
						
						result.setEditable(false);
						resultScroll.setVisible(false);
						
						resultPane.addTab(address.toString(), resultScroll);
						
						execute(result, address);
					}
				} catch (IOException e) {
					e.printStackTrace();
					DialogUtil.showErrorDialog(ConfigurationExecutor.this, e);
				}
				
				status.setText("Finished execution");
			}
		}.start();
	}
	
	public void open(File file) throws IOException {
		Configuration configuration = ConfigurationHelper.readFromJsonFile(
				file, new EncryptionCallback() {
					public String getPassword(Encryption encryption) {
						return ConfigurationDialog
								.showDecryptionDialog(ConfigurationExecutor.this);
					}
				});
		
		setConfiguration(configuration);
	}
	
	private void execute(JTextArea area, InetAddress address)
			throws IOException {
		DeviceAccessor accessor = new IPDeviceAccessor(address);
		ConnectionSettings settings = ConnectionContainer
				.getSettings(configuration);
		
		ConnectionContainer connectionContainer = configuration
				.getConnectionContainer();
		CommandLineInterface cli = (CommandLineInterface) connectionFactory
				.openConnection(connectionContainer.getConnectionType(),
						accessor, settings);
		
		InputStream inputStream = cli.getInputStream();
		OutputStream outputStream = cli.getOutputStream();
		
		if (connectionContainer == ConnectionContainer.TELNET) {
			String username = configuration.getUsername();
			String password = configuration.getPassword();
			
			if (!username.isEmpty()) {
				outputStream.write(username.getBytes());
				outputStream.write("\n".getBytes());
				outputStream.write(password.getBytes());
				outputStream.write("\n".getBytes());
				outputStream.flush();
			} else if (!password.isEmpty()) {
				outputStream.write(password.getBytes());
				outputStream.write("\n".getBytes());
				outputStream.flush();
			}
		}
		
		String batch = configuration.getBatch((String) batches
				.getSelectedItem());
		
		outputStream.write((batch + "\n" + BATCH_SUFFIX + "\n").getBytes());
		outputStream.flush();
		
		inputStream = new UntilLineMatchInputStream(inputStream,
				BATCH_END_PATTERN);
		
		String output = StreamUtil.readAsString(inputStream);
		area.setText(output);
		
		cli.close();
	}
	
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
		
		Iterator<InetAddress> addressIterator = configuration.getAddresses()
				.iterator();
		String addresses = addressIterator.next().getHostAddress();
		while (addressIterator.hasNext()) {
			addresses += ";" + addressIterator.next().getHostAddress();
		}
		address.setText(addresses);
		
		connection.setText(configuration.getConnectionContainer().getName());
		port.setText("" + configuration.getPort());
		
		batches.removeAllItems();
		for (String batchName : configuration.getBatches().keySet()) {
			batches.addItem(batchName);
		}
	}
}