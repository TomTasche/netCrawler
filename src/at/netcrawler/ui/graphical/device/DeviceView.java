package at.netcrawler.ui.graphical.device;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import at.netcrawler.network.manager.DeviceManager;
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
import at.netcrawler.ui.graphical.device.category.Category;
import at.netcrawler.ui.graphical.device.category.CiscoCategory;
import at.netcrawler.ui.graphical.device.category.CiscoRouterCategory;
import at.netcrawler.ui.graphical.device.category.CiscoSwitchCategory;
import at.netcrawler.ui.graphical.device.category.DeviceCategory;
import at.netcrawler.ui.graphical.device.category.RouterCategory;
import at.netcrawler.ui.graphical.device.category.SNMPCategory;
import at.netcrawler.util.NetworkDeviceHelper;


@SuppressWarnings("serial")
public class DeviceView extends JFrame implements NetworkModelListener {
	
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
	
	private final NetworkDevice device;
	private final DeviceManager manager;
	private final JScrollPane pane;
	
	public DeviceView(DeviceManager manager, TopologyDevice device) {
		this.manager = manager;
		this.device = device.getNetworkDevice();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Device View - "
				+ NetworkDeviceHelper.getHostname(this.device));
		
		pane = new JScrollPane();
		add(pane);
		
		build();
		
		this.device.addListener(this);
	}
	
	@Override
	public void valueChanged(String key, Object value, Object oldValue) {
		build();
	}
	
	@Override
	public void extensionAdded(NetworkModelExtension extension) {
		build();
	}
	
	@Override
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
				
				tabs.put(
						category.getCategory(), tab);
			}
			
			tab.addTab(
					category.getSub(), category.render(manager, device));
		}
		
		JTabbedPane leftTabs = new JTabbedPane(JTabbedPane.LEFT);
		for (Entry<String, JTabbedPane> entry : tabs.entrySet()) {
			leftTabs.addTab(
					entry.getKey(), entry.getValue());
		}
		
		pane.setViewportView(leftTabs);
		
		pack();
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
