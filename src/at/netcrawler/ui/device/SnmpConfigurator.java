package at.netcrawler.ui.device;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.util.ObjectIdentifier;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.snmp.LocalSNMPConnection;
import at.netcrawler.network.connection.snmp.SNMPSecurityLevel;
import at.netcrawler.network.connection.snmp.SNMPVersion;
import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.ui.DialogUtil;
import at.netcrawler.util.NetworkDeviceHelper;

@SuppressWarnings("serial")
public class SnmpConfigurator extends JFrame {

	private final NetworkDevice device;
	private JTextField oidField;
	private JTextArea resultArea;
	private JButton executeButton;

	public SnmpConfigurator(NetworkDevice device) {
		this.device = device;

		setLayout(new BorderLayout());

		oidField = new JTextField();
		executeButton = new JButton("Execute");

		JPanel oidPanel = new JPanel(new BorderLayout());
		oidPanel.add(oidField, BorderLayout.CENTER);
		oidPanel.add(executeButton, BorderLayout.EAST);

		resultArea = new JTextArea();
		resultArea.setEditable(false);

		executeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String oid = oidField.getText();
				if (oid.isEmpty() || !oid.contains(".")) {
					DialogUtil.showErrorDialog(SnmpConfigurator.this,
							"Couldn't parse OID.");
				} else {
					String value = getValue(oid);

					resultArea.setText("Value for OID: " + oid + ":"
							+ System.getProperty("line.separator") + value);

					// TODO: edit?
				}
			}
		});

		add(oidPanel, BorderLayout.NORTH);
		add(resultArea, BorderLayout.CENTER);
	}

	private String getValue(String oid) {
		// TODO: use information from NetworkDevice.CONNECTED_VIA
		// TODO: use DeviceManager?
		// TODO: allow walk, getnext, etc...
		IPv4Address ipAddress = new IPv4Address(NetworkDeviceHelper.getManagementAddresses(device));
		int port = 161;
		IPDeviceAccessor accessor = new IPDeviceAccessor(ipAddress);
		SNMPConnectionSettings settings = new SNMPConnectionSettings();
		settings.setPort(port);
		settings.setVersion(SNMPVersion.VERSION2C);
		settings.setCommunity("netCrawler");
		settings.setSecurityLevel(SNMPSecurityLevel.NOAUTH_NOPRIV);
		LocalSNMPConnection connection = new LocalSNMPConnection(accessor,
				settings);
		
		return connection.get(new ObjectIdentifier(oid)).getValue();
	}
}
