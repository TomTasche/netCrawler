package at.netcrawler.gui.main;

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
import javax.swing.JList;

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
		
		setModel(model);
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
		
		
		@SuppressWarnings("unchecked")
		@Override
		public Object getElementAt(int index) {
			NetworkDevice device = devices.get(index).getNetworkDevice();
			
			String name = (String) device.getValue(NetworkDevice.HOSTNAME);
			String caps = "";
			
			Set<Capability> capabilities = (Set<Capability>) device.getValue(NetworkDevice.CAPABILITIES);
			for (Capability capability : capabilities) {
				caps += capability.name().substring(0, 1);
			}
			
			return name + "(" + caps + ")";
		}
		
		public int getSize() {
			return topology.getVertexCount();
		}
	}
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(new Dimension(500, 500));
		
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
		frame.add(list);
		
		list.setTopology(topology);
		
		frame.setVisible(true);
	}
}
