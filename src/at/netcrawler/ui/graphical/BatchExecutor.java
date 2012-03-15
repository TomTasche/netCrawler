package at.netcrawler.ui.graphical;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle;
import javax.swing.filechooser.FileFilter;

import at.andiwand.library.cli.CommandLineInterface;
import at.andiwand.library.io.StreamUtil;
import at.netcrawler.assistant.Configuration;
import at.netcrawler.assistant.ConfigurationDialog;
import at.netcrawler.assistant.ConnectionContainer;
import at.netcrawler.assistant.Encryption;
import at.netcrawler.assistant.EncryptionCallback;
import at.netcrawler.io.UntilLineMatchInputStream;
import at.netcrawler.network.accessor.DeviceAccessor;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ConnectionBuilder;
import at.netcrawler.network.connection.ConnectionSettings;
import at.netcrawler.network.connection.ssh.LocalSSHGateway;
import at.netcrawler.network.connection.telnet.LocalTelnetGateway;


public class BatchExecutor extends JFrame {
	
	private static final long serialVersionUID = 7767764252271031663L;
	
	private static final String TITLE = "Batch Executor";
	
	private static final String BATCH_SUFFIX = "!executorEnd";
	
	private JLabel address = new JLabel();
	private JLabel connection = new JLabel();
	private JLabel port = new JLabel();
	private JComboBox batches = new JComboBox();
	private JButton execute = new JButton("Execute");
	private JToggleButton resultButton = new JToggleButton("Show result");
	private JTextArea result = new JTextArea();
	private JScrollPane resultScroll = new JScrollPane(result);
	private JLabel status = new JLabel();
	
	private JFileChooser fileChooser = new JFileChooser();
	
	private Configuration configuration;
	
	private ConnectionBuilder connectionFactory = new ConnectionBuilder(
			new LocalTelnetGateway(), new LocalSSHGateway());
	
	public BatchExecutor(Configuration configuration) {
		this();
		
		setEnabledAll(true);
		
		setConfiguration(configuration);
	}
	
	public BatchExecutor() {
		setTitle(TITLE);
		
		JPanel panel = new JPanel();
		JLabel addressLabel = new JLabel("Address:");
		JLabel connectionLabel = new JLabel("Connection:");
		JLabel portLabel = new JLabel("Port:");
		JLabel batchLabel = new JLabel("Batch:");
		JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		
		result.setEditable(false);
		resultScroll.setVisible(false);
		
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
				.addComponent(resultScroll, Alignment.TRAILING)
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
				.addComponent(resultScroll)
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
				resultScroll.setVisible(resultButton.isSelected());
				
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
				BatchExecutor.this.dispose();
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
			ConfigurationDialog.showErrorDialog(this, e);
		}
	}
	
	public void doExecute() {
		new Thread() {
			@Override
			public void run() {
				status.setText("Executing...");
				
				try {
					execute();
				} catch (IOException e) {
					e.printStackTrace();
					ConfigurationDialog.showErrorDialog(BatchExecutor.this, e);
				}
				
				status.setText("Finished execution");
			}
		}.start();
	}
	
	public void open(File file) throws IOException {
		Configuration configuration = new Configuration();
		configuration.readFromJsonFile(file, new EncryptionCallback() {
			public String getPassword(Encryption encryption) {
				return ConfigurationDialog
						.showDecryptionDialog(BatchExecutor.this);
			}
		});
		
		setConfiguration(configuration);
	}
	
	private void execute() throws IOException {
		DeviceAccessor accessor = new IPDeviceAccessor(configuration
				.getAddress());
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
		
		Pattern endPattern = Pattern.compile(".+" + BATCH_SUFFIX);
		String batch = configuration.getBatch((String) batches
				.getSelectedItem());
		
		outputStream.write((batch + "\n" + BATCH_SUFFIX + "\n").getBytes());
		outputStream.flush();
		
		inputStream = new UntilLineMatchInputStream(inputStream, endPattern);
		
		String output = StreamUtil.readAsString(inputStream);
		result.setText(output);
		
		cli.close();
	}
	
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
		
		address.setText(configuration.getAddress().toString());
		connection.setText(configuration.getConnectionContainer().getName());
		port.setText("" + configuration.getPort());
		
		batches.removeAllItems();
		for (String batchName : configuration.getBatches().keySet()) {
			batches.addItem(batchName);
		}
	}
}