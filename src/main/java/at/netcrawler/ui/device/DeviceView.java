package at.netcrawler.ui.device;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import at.andiwand.library.component.JFrameUtil;
import at.netcrawler.network.connection.CommandLineConnection;
import at.netcrawler.network.connection.Connection;
import at.netcrawler.network.connection.ConnectionBuilder;
import at.netcrawler.network.connection.ConnectionType;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;
import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.manager.DeviceManagerBuilder;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;
import at.netcrawler.network.model.NetworkModelExtension;
import at.netcrawler.network.model.NetworkModelListener;
import at.netcrawler.network.model.extension.CiscoDeviceExtension;
import at.netcrawler.network.model.extension.CiscoRouterExtension;
import at.netcrawler.network.model.extension.CiscoSwitchExtension;
import at.netcrawler.network.model.extension.RouterExtension;
import at.netcrawler.network.model.extension.SNMPDeviceExtension;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.ui.device.category.Category;
import at.netcrawler.ui.device.category.CiscoCategory;
import at.netcrawler.ui.device.category.CiscoRouterCategory;
import at.netcrawler.ui.device.category.CiscoSwitchCategory;
import at.netcrawler.ui.device.category.DeviceCategory;
import at.netcrawler.ui.device.category.RouterCategory;
import at.netcrawler.ui.device.category.SNMPCategory;
import at.netcrawler.util.DialogUtil;
import at.netcrawler.util.NetworkDeviceHelper;


// TODO: update model / fetch
@SuppressWarnings("serial")
public class DeviceView extends JFrame implements NetworkModelListener {
	
	private final static Map<Class<? extends NetworkModelExtension>, Category> EXTENSION_CATEGORY_MAPPING = new HashMap<Class<? extends NetworkModelExtension>, Category>();
	
	static {
		EXTENSION_CATEGORY_MAPPING.put(RouterExtension.class,
				new RouterCategory());
		EXTENSION_CATEGORY_MAPPING.put(CiscoDeviceExtension.class,
				new CiscoCategory());
		EXTENSION_CATEGORY_MAPPING.put(SNMPDeviceExtension.class,
				new SNMPCategory());
		EXTENSION_CATEGORY_MAPPING.put(NetworkDeviceExtension.class,
				new DeviceCategory());
		EXTENSION_CATEGORY_MAPPING.put(CiscoRouterExtension.class,
				new CiscoRouterCategory());
		EXTENSION_CATEGORY_MAPPING.put(CiscoSwitchExtension.class,
				new CiscoSwitchCategory());
	}
	
	private final NetworkDevice device;
	private final DeviceManager manager;
	private final SSHSettings settings;
	private final Connection connection;
	
	public DeviceView(TopologyDevice device) throws IOException {
		addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {
				try {
					connection.close();
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}
		});
		
		settings = new SSHSettings(SSHVersion.VERSION2);
		// TODO: OLOLO
		settings.setPassword("cisco");
		settings.setUsername("cisco");
		
		connection = ConnectionBuilder.getLocalConnectionBuilder()
				.openConnection(ConnectionType.SSH, device.getNetworkDevice(),
						settings);
		
		this.manager = new DeviceManagerBuilder().buildDeviceManager(device
				.getNetworkDevice(), connection);
		this.device = device.getNetworkDevice();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Device View - "
				+ NetworkDeviceHelper.getHostname(this.device));
		
		JMenuBar bar = new JMenuBar();
		// TODO: only display protocols we're actually able to use for this
		// device (NetworkDevice.CONNECTED_VIA)
		JMenu advancedMenu = new JMenu("Advanced");
		final JMenuItem snmpItem = new JMenuItem("SNMP");
		final JMenuItem terminalItem = new JMenuItem("Terminal");
		snmpItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SnmpConfigurator(DeviceView.this.device);
			}
		});
		terminalItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: allow configuring settings first, like SnmpConfigurator
				String title = "Terminal - "
						+ NetworkDeviceHelper
								.getHostname(DeviceView.this.device);
				
				try {
					CommandLineConnection connection = ConnectionBuilder
							.getLocalConnectionBuilder().openConnection(
									ConnectionType.SSH, DeviceView.this.device,
									settings);
					// if (connection == null) connection =
					// ConnectionBuilder.getLocalConnectionBuilder().openConnection(ConnectionType.SSH,
					// DeviceView.this.device, new
					// SSHSettings(SSHVersion.VERSION1));
					// if (connection == null) connection =
					// ConnectionBuilder.getLocalConnectionBuilder().openConnection(ConnectionType.SSH,
					// DeviceView.this.device, new TelnetSettings());
					
					JFrame frame = new JSimpleTerminal(title, connection);
					JFrameUtil.centerFrame(frame);
					frame.setVisible(true);
				} catch (IOException ex) {
					ex.printStackTrace();
					
					DialogUtil.showErrorDialog(DeviceView.this, ex);
				}
			}
		});
		
		advancedMenu.add(snmpItem);
		advancedMenu.add(terminalItem);
		
		bar.add(advancedMenu);
		
		setJMenuBar(bar);
		
		build();
		
		this.device.addListener(this);
	}
	
	public void valueChanged(String key, Object value, Object oldValue) {
		build();
	}
	
	public void extensionAdded(NetworkModelExtension extension) {
		build();
	}
	
	public void extensionRemoved(NetworkModelExtension extension) {
		build();
	}
	
	private void build() {
		Map<String, JTabbedPane> tabs = new HashMap<String, JTabbedPane>();
		List<Category> categories = buildCategories();
		for (Category category : categories) {
			JTabbedPane tab = tabs.get(category.getCategory());
			if (tab == null) {
				tab = new JTabbedPane(JTabbedPane.TOP);
				
				tabs.put(category.getCategory(), tab);
			}
			
			tab.addTab(category.getSub(), category.render(manager, device));
		}
		
		JTabbedPane leftTabs = new JTabbedPane(JTabbedPane.LEFT);
		for (Entry<String, JTabbedPane> entry : tabs.entrySet()) {
			leftTabs.addTab(entry.getKey(), entry.getValue());
		}
		
		add(leftTabs);
		
		setSize(600, 300);
		setMinimumSize(getSize());
	}
	
	private List<Category> buildCategories() {
		List<Category> categories = new LinkedList<Category>();
		
		// TODO: remove, because NetworkDeviceExtension is automatically added?
		if (device instanceof NetworkDevice) {
			categories.add(new DeviceCategory());
		}
		
		for (NetworkModelExtension extension : device.getExtensions()) {
			Category category = EXTENSION_CATEGORY_MAPPING.get(extension
					.getClass());
			
			categories.add(category);
		}
		
		return categories;
	}
	
}
