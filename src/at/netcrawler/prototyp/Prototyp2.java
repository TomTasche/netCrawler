package at.netcrawler.prototyp;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import at.andiwand.library.cli.CommandLine;
import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.network.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.LocalSSHConnection;
import at.netcrawler.network.connection.ssh.SSHConnectionSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;
import at.netcrawler.network.connection.telnet.LocalTelnetConnection;
import at.netcrawler.network.connection.telnet.TelnetConnectionSettings;
import at.netcrawler.stream.IgnoreFirstLineInputStream;
import at.netcrawler.stream.IgnoreLastLineInputStream;
import at.netcrawler.stream.ReadUntilMatchInputStream;
import at.netcrawler.util.StreamUtil;


public class Prototyp2 extends JFrame {

	private static final long serialVersionUID = 890687415300742520L;

	final String SSH_1 = "SSH v1";
	final String SSH_2 = "SSH v2";
	final String TELNET = "Telnet";	

	JPanel panel = new JPanel();
	
	JLabel ipLabel = new JLabel("IP:");
	JLabel connectionLabel = new JLabel("Connection:");
	JLabel portLabel = new JLabel("Port:");
	JLabel usernameLabel = new JLabel("Username:");
	JLabel passwordLabel = new JLabel("Password:");
	JLabel batchLabel  = new JLabel("Batch:");
	
	JTextField ipField = new JTextField("192.168.0.254");
	JComboBox connectionBox = new JComboBox(new String[] {SSH_2, SSH_1, TELNET});
	JTextField portField = new JTextField("22");
	JTextField usernameField = new JTextField("cisco");
	JPasswordField passwordField = new JPasswordField("cisco");
	JTextArea responseArea = new JTextArea();
	JTextArea batchArea = new JTextArea();
	
	
	JButton fileButton = new JButton("Choose batch");
	JButton execute = new JButton("Execute");
		
	JFileChooser fileChooser = new JFileChooser();
	
	JScrollPane batchScrollPane = new JScrollPane(batchArea);
	JScrollPane responseScrollPane = new JScrollPane(responseArea);


	public Prototyp2() {
		setTitle("Batcheria");
		
		batchScrollPane.setPreferredSize(new Dimension(200, 400));
		responseArea.setEditable(false);
		responseScrollPane.setPreferredSize(new Dimension(200, 400));
		
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(ipLabel)
						.addComponent(connectionLabel)
						.addComponent(portLabel)
						.addComponent(usernameLabel)
						.addComponent(passwordLabel)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(ipField)
						.addComponent(connectionBox)
						.addComponent(portField)
						.addComponent(usernameField)
						.addComponent(passwordField)
						.addGroup(layout.createSequentialGroup()
								.addComponent(fileButton)
								.addComponent(execute)
						)
						.addGroup(layout.createSequentialGroup()
								.addComponent(batchScrollPane)
								.addComponent(responseScrollPane)
						)
				)
		);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
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
						.addComponent(fileButton)
						.addComponent(execute)
				)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(batchScrollPane)
						.addComponent(responseScrollPane)
				)
		);

		fileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileChooser.showOpenDialog(Prototyp2.this);

				File batchFile = fileChooser.getSelectedFile();
				
				if (batchFile == null) {
					return;
				}
				
				try {
					FileInputStream inputStream = new FileInputStream(batchFile);
					
					String batch = StreamUtil.readStream(inputStream);
					batchArea.setText(batch);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		execute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					responseArea.setText("");
					
					String output = openConnection(usernameField.getText(),
							new String(passwordField.getPassword()),
							ipField.getText(), batchArea.getText(),
							connectionBox.getSelectedItem().toString(),
							Integer.parseInt(portField.getText()));
					
					responseArea.setText(output);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		connectionBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String connection = (String) connectionBox.getSelectedItem();
				
				if (connection.equals(SSH_2)) {
					portField.setText("22");
				} else if (connection.equals(SSH_1)) {
					portField.setText("22");
				} else if (connection.equals(TELNET)) {
					portField.setText("23");
				}
			}
		});
		
		add(panel);
		
		pack();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dimension.width - getWidth()) >> 1,
				(dimension.height - getHeight()) >> 1);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}


	private String openConnection(String username, String password, String ip,
			String batch, String connection, int port) throws IOException {
		CommandLine commandLine;
		
		IPv4Address ipAddress = IPv4Address.getByAddress(ip);
		IPDeviceAccessor accessor = new IPDeviceAccessor(ipAddress);
		
		if (connection.equals(SSH_2)) {
			SSHConnectionSettings settings = new SSHConnectionSettings();
			settings.setVersion(SSHVersion.VERSION2);
			settings.setPort(port);
			settings.setUsername(username);
			settings.setPassword(password);
			
			commandLine = new LocalSSHConnection(accessor, settings);
		} else if (connection.equals(SSH_1)) {
			SSHConnectionSettings settings = new SSHConnectionSettings();
			settings.setVersion(SSHVersion.VERSION1);
			settings.setPort(port);
			settings.setUsername(username);
			settings.setPassword(password);
			
			commandLine = new LocalSSHConnection(accessor, settings);
		} else if (connection.equals(TELNET)) {
			TelnetConnectionSettings settings = new TelnetConnectionSettings();
			settings.setPort(port);
			
			commandLine = new LocalTelnetConnection(accessor, settings);
			
			OutputStream outputStream = commandLine.getOutputStream();
			
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
		
		InputStream inputStream = commandLine.getInputStream();
		OutputStream outputStream = commandLine.getOutputStream();
		
		String end = "!asdf1234asdf";
		Pattern endPattern = Pattern.compile(".*" + end);
		
		outputStream.write(("\n" + batch + "\n" + end + "\n").getBytes());
		
		inputStream = new IgnoreFirstLineInputStream(inputStream);
		inputStream = new ReadUntilMatchInputStream(inputStream, endPattern);
		inputStream = new IgnoreLastLineInputStream(inputStream);
		
		String result = StreamUtil.readStream(inputStream);
		
		commandLine.close();
		
		return result;
	}


	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		new Prototyp2();
	}
}
