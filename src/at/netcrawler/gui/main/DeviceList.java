package at.netcrawler.gui.main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import at.netcrawler.network.Capability;
import at.netcrawler.network.model.NetworkCable;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.topology.HashTopology;
import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyCable;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.network.topology.TopologyInterface;

@SuppressWarnings("serial")
public class DeviceList extends JList {

	Topology topology;
	DeviceListModel model;
	
	
	public DeviceList() {
		model = new DeviceListModel();
		
		// setCellRenderer(new DeviceCellRenderer());
		setModel(model);
//		setPreferredSize(new Dimension(100, 20));
	}
	
	
	public void setTopology(Topology topology) {
		this.topology = topology;
		
		model.setDevices(topology.getVertices());
	}
	
	
	private class DeviceListModel extends AbstractListModel {
		
		List<TopologyDevice> devices = new ArrayList<TopologyDevice>();
		
		
		public void setDevices(Collection<TopologyDevice> devices) {
			int oldLength = devices.size();
			
			this.devices = Collections.unmodifiableList(new ArrayList<TopologyDevice>(devices));
			
			fireContentsChanged(this, 0, oldLength);
		}
		
		
		@Override
		public Object getElementAt(int index) {
			NetworkDevice device = devices.get(index).getNetworkDevice();
			
			String name = NetworkDeviceHelper.getHostname(device);
			String caps = NetworkDeviceHelper.concatCapabilities(device);
			
			return name + "(" + caps + ")";
		}
		
		public int getSize() {
			return topology.getVertexCount();
		}
	}
	
	private class DeviceCellRenderer implements ListCellRenderer {
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			return new JLabel((String) value);
		}
	}
	
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Topology topology = new HashTopology();
		
		NetworkDevice deviceA = new NetworkDevice();
		NetworkInterface interfaceA = new NetworkInterface();
		interfaceA.setValue(NetworkInterface.NAME, "eth0");
		deviceA.setValue(NetworkDevice.HOSTNAME, "A");
		deviceA.setValue(NetworkDevice.INTERFACES,
				new HashSet<NetworkInterface>(Arrays.asList(interfaceA)));
		
		NetworkDevice deviceB = new NetworkDevice();
		NetworkInterface interfaceB = new NetworkInterface();
		interfaceB.setValue(NetworkInterface.NAME, "eth0");
		deviceB.setValue(NetworkDevice.HOSTNAME, "B");
		deviceB.setValue(NetworkDevice.INTERFACES,
				new HashSet<NetworkInterface>(Arrays.asList(interfaceB)));
		
		NetworkCable cable = new NetworkCable();
		
		TopologyDevice topologyDeviceA = new TopologyDevice(deviceA);
		TopologyInterface topologyInterfaceA = topologyDeviceA.getInterfaceByName("eth0");
		TopologyDevice topologyDeviceB = new TopologyDevice(deviceB);
		TopologyInterface topologyInterfaceB = topologyDeviceB.getInterfaceByName("eth0");
		TopologyCable topologyCable = new TopologyCable(cable,
				new HashSet<TopologyInterface>(Arrays.asList(
						topologyInterfaceA, topologyInterfaceB)));
		
		topology.addVertex(topologyDeviceA);
		topology.addVertex(topologyDeviceB);
		topology.addEdge(topologyCable);
		
		deviceA.setValue(NetworkDevice.MAJOR_CAPABILITY, Capability.ROUTER);
		deviceA.setValue(NetworkDevice.CAPABILITIES, new HashSet<Capability>() {{add(Capability.ROUTER);}});
		
		deviceB.setValue(NetworkDevice.MAJOR_CAPABILITY, Capability.SWITCH);
		deviceB.setValue(NetworkDevice.CAPABILITIES, new HashSet<Capability>() {{add(Capability.SWITCH);}});
		
		DeviceList list = new DeviceList();
		frame.add(new JScrollPane(list));
		
		list.setTopology(topology);
		
		frame.pack();
		frame.setVisible(true);
	}
}
