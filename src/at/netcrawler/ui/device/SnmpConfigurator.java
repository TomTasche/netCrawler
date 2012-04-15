package at.netcrawler.ui.device;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import at.andiwand.library.component.JFrameUtil;
import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.util.ObjectIdentifier;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.snmp.LocalSNMPConnection;
import at.netcrawler.network.connection.snmp.SNMPSecurityLevel;
import at.netcrawler.network.connection.snmp.SNMPSettings;
import at.netcrawler.network.connection.snmp.SNMPVersion;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.util.DialogUtil;
import at.netcrawler.util.GridBagLayoutUtil;
import at.netcrawler.util.NetworkDeviceHelper;

@SuppressWarnings("serial")
public class SnmpConfigurator extends JFrame {

	private NetworkDevice device;
	private JTextField oidField;
	private JTextArea resultArea;
	private JButton saveButton;
	private JLabel executeStatusLabel;
	private JLabel saveStatusLabel;

	public SnmpConfigurator(NetworkDevice device) {
		//		super("SNMP - " + NetworkDeviceHelper.getHostname(device));
		super("SNMP");

		this.device = device;

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JLabel addressLabel = new JLabel("Address:");
		JLabel portLabel = new JLabel("Port:");
		JLabel versionLabel = new JLabel("Version:");
		JLabel communityLabel = new JLabel("Community:");
		JLabel userLabel = new JLabel("Username:");
		JLabel passwordLabel = new JLabel("Password:");
		JLabel cryptoLabel = new JLabel("Crypto Key:");
		JLabel oidLabel = new JLabel("Object Identifier:");
		JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		executeStatusLabel = new JLabel();
		saveStatusLabel = new JLabel();

		JTextField addressField = new JTextField();
		JTextField portField = new JTextField();
		JComboBox versionBox = new JComboBox(SNMPVersion.values());
		JTextField communityField = new JTextField();
		JTextField userField = new JTextField();
		JPasswordField passwordField = new JPasswordField();
		JPasswordField cryptoField = new JPasswordField();
		oidField = new JTextField();
		resultArea = new JTextArea();
		resultArea.setEnabled(false);
		JButton executeButton = new JButton("Execute");
		saveButton = new JButton("Save");
		saveButton.setEnabled(false);
		
		GridBagLayoutUtil util = new GridBagLayoutUtil(2);
		util.add(addressLabel);
		util.add(addressField);
		util.add(portLabel);
		util.add(portField);
		util.add(versionLabel);
		util.add(versionBox);
		util.add(communityLabel);
		util.add(communityField);
		util.add(userLabel);
		util.add(userField);
		util.add(passwordLabel);
		util.add(passwordField);
		util.add(cryptoLabel);
		util.add(cryptoField);
		util.add(oidLabel);
		util.add(oidField);
		util.add(executeStatusLabel);
		util.add(executeButton);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = util.getWeight();
		constraints.gridx = 0;
		constraints.gridy = util.getRow();
		constraints.gridwidth = util.getColumns();
		constraints.insets = util.getInsets();
		util.add(separator, constraints);
		
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = util.getRow();
		constraints.gridwidth = util.getColumns();
		constraints.insets = util.getInsets();
		util.add(new JScrollPane(resultArea), constraints);
		
		util.add(saveStatusLabel);
		util.add(saveButton);
		
		add(util.getPanel());

		executeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String oid = oidField.getText();
				if (oid.isEmpty() || !oid.contains(".")) {
					DialogUtil.showErrorDialog(SnmpConfigurator.this,
							"Couldn't parse OID.");
				} else {
					String value = getValue(oid);
					if (value == null) {
						executeStatusLabel.setText("Failed.");
					} else {
						executeStatusLabel.setText("Success.");
						
						resultArea.setText("Value for OID: " + oid + ":"
								+ System.getProperty("line.separator") + value);

						resultArea.setEditable(true);
						resultArea.setEnabled(true);
						saveButton.setEnabled(true);
					}
				}
			}
		});

		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String oid = oidField.getText();
				if (oid.isEmpty() || !oid.contains(".")) {
					DialogUtil.showErrorDialog(SnmpConfigurator.this,
							"Couldn't parse OID.");
				} else {
					if (setValue(oid)) {
						saveStatusLabel.setText("Success.");
					} else {
						saveStatusLabel.setText("Failed.");
					}
				}
			}
		});

		resultArea.setPreferredSize(new Dimension((int) resultArea.getPreferredScrollableViewportSize().getHeight(), 100));
		passwordField.setPreferredSize(new Dimension(150, passwordField
				.getPreferredSize().height));

		pack();

		JFrameUtil.centerFrame(this);
		setVisible(true);
	}

	private String getValue(String oid) {
		// TODO: use information from NetworkDevice.CONNECTED_VIA
		// TODO: use DeviceManager?
		// TODO: allow walk, getnext, etc...
		IPv4Address ipAddress = new IPv4Address(NetworkDeviceHelper.getManagementAddresses(device));
		int port = 161;
		IPDeviceAccessor accessor = new IPDeviceAccessor(ipAddress);
		SNMPSettings settings = new SNMPSettings();
		settings.setPort(port);
		settings.setVersion(SNMPVersion.VERSION2C);
		settings.setCommunity("netCrawler");
		settings.setSecurityLevel(SNMPSecurityLevel.NOAUTH_NOPRIV);
		// TODO: ...
		LocalSNMPConnection connection = new LocalSNMPConnection(accessor,
				settings);

		return connection.get(new ObjectIdentifier(oid)).getValue();
	}

	private boolean setValue(String oid) {
		// TODO:
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		new SnmpConfigurator(null);
	}
}
