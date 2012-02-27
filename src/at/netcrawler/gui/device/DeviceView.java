package at.netcrawler.gui.device;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import at.netcrawler.gui.device.category.Category;
import at.netcrawler.gui.device.category.CiscoCategory;
import at.netcrawler.gui.device.category.CiscoRouterCategory;
import at.netcrawler.gui.device.category.CiscoSwitchCategory;
import at.netcrawler.gui.device.category.DeviceCategory;
import at.netcrawler.gui.device.category.RouterCategory;
import at.netcrawler.gui.device.category.SNMPCategory;
import at.netcrawler.gui.main.NetworkDeviceHelper;
import at.netcrawler.network.Capability;
import at.netcrawler.network.model.NetworkCable;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkDeviceExtension;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.model.NetworkModel;
import at.netcrawler.network.model.NetworkModelExtension;
import at.netcrawler.network.model.extension.CiscoDeviceExtension;
import at.netcrawler.network.model.extension.CiscoRouterExtension;
import at.netcrawler.network.model.extension.CiscoSwitchExtension;
import at.netcrawler.network.model.extension.RouterExtension;
import at.netcrawler.network.model.extension.SNMPDeviceExtension;
import at.netcrawler.network.topology.HashTopology;
import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyCable;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.network.topology.TopologyInterface;


@SuppressWarnings("serial")
public class DeviceView extends JFrame {
	
	private final static Map<Class<? extends NetworkModelExtension>, Category> EXTENSION_CATEGORY_MAPPING = new HashMap<Class<? extends NetworkModelExtension>, Category>();
	
	static {
		EXTENSION_CATEGORY_MAPPING.put(
				RouterExtension.class, new RouterCategory());
		EXTENSION_CATEGORY_MAPPING.put(
				CiscoDeviceExtension.class, new CiscoCategory());
		EXTENSION_CATEGORY_MAPPING.put(
				SNMPDeviceExtension.class, new SNMPCategory());
		EXTENSION_CATEGORY_MAPPING.put(
				NetworkDeviceExtension.class, new DeviceCategory());
		EXTENSION_CATEGORY_MAPPING.put(
				CiscoRouterExtension.class, new CiscoRouterCategory());
		EXTENSION_CATEGORY_MAPPING.put(
				CiscoSwitchExtension.class, new CiscoSwitchCategory());
	}
	
	private final NetworkModel device;
	
	public DeviceView(NetworkModel device) {
		this.device = device;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Device View - " + NetworkDeviceHelper.getHostname(device));
		
		Map<String, JTabbedPane> tabs = new HashMap<String, JTabbedPane>();
		List<Category> categories = buildCategories();
		for (Category category : categories) {
			JTabbedPane tab = tabs.get(category.getCategory());
			if (tab == null) {
				tab = new JTabbedPane(JTabbedPane.TOP);
				
				tabs.put(
						category.getCategory(), tab);
			}
			
			tab.addTab(
					category.getSub(), category.render(device));
		}
		
		JTabbedPane leftTabs = new JTabbedPane(JTabbedPane.LEFT);
		for (Entry<String, JTabbedPane> entry : tabs.entrySet()) {
			leftTabs.addTab(
					entry.getKey(), entry.getValue());
		}
		
		add(new JScrollPane(leftTabs));
		
		pack();
		setMinimumSize(getSize());
		setVisible(true);
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
	
	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		Topology topology = new HashTopology();
		
		NetworkDevice deviceA = new NetworkDevice();
		NetworkInterface interfaceA = new NetworkInterface();
		interfaceA.setValue(
				NetworkInterface.NAME, "eth0");
		deviceA.setValue(
				NetworkDevice.HOSTNAME, "A");
		deviceA.setValue(
				NetworkDevice.INTERFACES,
				new HashSet<NetworkInterface>(Arrays.asList(interfaceA)));
		
		NetworkDevice deviceB = new NetworkDevice();
		NetworkInterface interfaceB = new NetworkInterface();
		interfaceB.setValue(
				NetworkInterface.NAME, "eth0");
		deviceB.setValue(
				NetworkDevice.HOSTNAME, "B");
		deviceB.setValue(
				NetworkDevice.INTERFACES,
				new HashSet<NetworkInterface>(Arrays.asList(interfaceB)));
		
		NetworkCable cable = new NetworkCable();
		
		TopologyDevice topologyDeviceA = new TopologyDevice(deviceA);
		TopologyInterface topologyInterfaceA = topologyDeviceA
				.getInterfaceByName("eth0");
		TopologyDevice topologyDeviceB = new TopologyDevice(deviceB);
		TopologyInterface topologyInterfaceB = topologyDeviceB
				.getInterfaceByName("eth0");
		TopologyCable topologyCable = new TopologyCable(cable,
				new HashSet<TopologyInterface>(Arrays.asList(
						topologyInterfaceA, topologyInterfaceB)));
		
		topology.addVertex(topologyDeviceA);
		topology.addVertex(topologyDeviceB);
		topology.addEdge(topologyCable);
		
		deviceA.setValue(
				NetworkDevice.MAJOR_CAPABILITY, Capability.ROUTER);
		deviceA.setValue(
				NetworkDevice.CAPABILITIES, new HashSet<Capability>() {
					{
						add(Capability.ROUTER);
						add(Capability.FIREWALL);
					}
				});
		
		deviceB.setValue(
				NetworkDevice.MAJOR_CAPABILITY, Capability.SWITCH);
		deviceB.setValue(
				NetworkDevice.CAPABILITIES, new HashSet<Capability>() {
					{
						add(Capability.SWITCH);
					}
				});
		
		new DeviceView(deviceA);
	}
}
