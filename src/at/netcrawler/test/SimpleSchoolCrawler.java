package at.netcrawler.test;

import java.awt.Dimension;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import at.andiwand.library.cli.CommandLine;
import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.cli.agent.CiscoCommandLineAgent;
import at.netcrawler.cli.agent.CiscoCommandLineAgentSettings;
import at.netcrawler.network.CDPNeighbors;
import at.netcrawler.network.accessor.DeviceAccessor;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ConnectionSettings;
import at.netcrawler.network.connection.ssh.LocalSSHConnection;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;
import at.netcrawler.network.connection.telnet.LocalTelnetConnection;
import at.netcrawler.network.connection.telnet.TelnetSettings;
import at.netcrawler.network.manager.cli.CiscoCommandLineDeviceManager;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.extension.CiscoExtension;


public class SimpleSchoolCrawler {
	
	public static class Logon {
		public String username;
		public String password;
	}
	
	public static Logon getLogon() {
		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		JLabel label = new JLabel("Choose your logon settings:");
		JLabel usernameLabel = new JLabel("Username:");
		JLabel passwordLabel = new JLabel("Password:");
		final JTextField usernameField = new JTextField();
		final JPasswordField passwordField = new JPasswordField();
		
		usernameField.setPreferredSize(new Dimension(150,
				usernameField.getPreferredSize().height));
		
		//@formatter:off
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(label)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addComponent(usernameLabel)
								.addComponent(passwordLabel)
						)
						.addGroup(layout.createParallelGroup()
								.addComponent(usernameField)
								.addComponent(passwordField)
						)
				)
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(label)
				.addGap(20)
				.addGroup(layout.createParallelGroup()
						.addComponent(usernameLabel)
						.addComponent(usernameField)
				)
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
				usernameField.requestFocusInWindow();
			}
		};
		
		JDialog dialog = optionPane.createDialog("Logon");
		dialog.setVisible(true);
		dialog.dispose();
		
		if (usernameField.getText().isEmpty()) return null;
		if (passwordField.getPassword().length == 0) return null;
		
		Logon logon = new Logon();
		logon.username = usernameField.getText();
		logon.password = new String(passwordField.getPassword());
		
		return logon;
	}
	
	public static IPv4Address getRootAddress() {
		final JTextField rootAddress = new JTextField();
		
		JOptionPane optionPane = new JOptionPane(rootAddress,
				JOptionPane.QUESTION_MESSAGE) {
			private static final long serialVersionUID = 646321718244222228L;
			
			public void selectInitialValue() {
				rootAddress.requestFocusInWindow();
			}
		};
		
		JDialog dialog = optionPane.createDialog("Root Address");
		dialog.setVisible(true);
		dialog.dispose();
		
		return IPv4Address.getByAddress(rootAddress.getText());
	}
	
	public static void main(String[] args) throws Throwable {
		IPv4Address rootAddress = getRootAddress();
		Logon logon = getLogon();
		Set<String> usedIDs = new HashSet<String>();
		
		crawlDevice(rootAddress, logon, usedIDs);
	}
	
	public static void crawlDevice(IPv4Address address, Logon logon,
			Set<String> usedIDs) throws IOException {
		DeviceAccessor accessor = new IPDeviceAccessor(address);
		ConnectionSettings settings;
		CommandLine commandLine = null;
		
		for (int i = 0; i < 3; i++) {
			settings = generateSettings(i, logon);
			
			try {
				commandLine = openConnection(accessor, settings);
				break;
			} catch (IOException e) {}
		}
		
		if (commandLine == null) throw new IOException("not able to connect!");
		
		CiscoCommandLineAgentSettings agentSettings = new CiscoCommandLineAgentSettings();
		agentSettings.setLogonUsername(logon.username);
		agentSettings.setLogonPassword(logon.password);
		
		CiscoCommandLineAgent agent = new CiscoCommandLineAgent(commandLine,
				agentSettings);
		
		NetworkDevice device = new NetworkDevice();
		CiscoCommandLineDeviceManager deviceManager = new CiscoCommandLineDeviceManager(
				device, agent);
		
		String id = deviceManager.getIdentication();
		if (usedIDs.contains(id)) {
			commandLine.close();
			return;
		}
		
		deviceManager.readDevice();
		
		commandLine.close();
		
		System.out.println();
		System.out.println("hostname:		"
				+ device.getValue(NetworkDevice.HOSTNAME));
		System.out.println("model number:		"
				+ device.getValue(CiscoExtension.MODEL_NUMBER));
		System.out.println("serial number:		"
				+ device.getValue(CiscoExtension.SYSTEM_SERIAL_NUMBER));
		System.out.println("processor:		"
				+ device.getValue(CiscoExtension.PROCESSOR_STRING));
		System.out.println("processor board id:	"
				+ device.getValue(NetworkDevice.IDENTICATION));
		
		usedIDs.add(id);
		
		CDPNeighbors neighbors = (CDPNeighbors) device.getValue(CiscoExtension.CDP_NEIGHBORS);
		for (CDPNeighbors.Neighbor neighbor : neighbors) {
			Iterator<IPv4Address> neighborAddressIterator = neighbor.getManagementAddresses().iterator();
			if (!neighborAddressIterator.hasNext()) continue;
			
			IPv4Address neighborAddress = neighborAddressIterator.next();
			
			crawlDevice(neighborAddress, logon, usedIDs);
		}
	}
	
	public static ConnectionSettings generateSettings(int i, Logon logon) {
		switch (i) {
		case 0:
		case 1:
			SSHSettings sshSettings = new SSHSettings();
			
			if (i == 0) sshSettings.setVersion(SSHVersion.VERSION2);
			else sshSettings.setVersion(SSHVersion.VERSION1);
			sshSettings.setUsername(logon.username);
			sshSettings.setPassword(logon.password);
			
			return sshSettings;
		case 2:
			TelnetSettings telnetSettings = new TelnetSettings();
			
			return telnetSettings;
			
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public static CommandLine openConnection(DeviceAccessor accessor,
			ConnectionSettings settings) throws IOException {
		if (settings.getClass().equals(SSHSettings.class)) {
			LocalSSHConnection connection = new LocalSSHConnection();
			connection.connect(accessor, settings);
			return connection;
		} else if (settings.getClass().equals(TelnetSettings.class)) {
			LocalTelnetConnection connection = new LocalTelnetConnection();
			connection.connect(accessor, settings);
			return connection;
		}
		
		throw new IllegalStateException("Unreachable section");
	}
}