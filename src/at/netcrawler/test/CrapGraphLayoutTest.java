package at.netcrawler.test;

import java.awt.RenderingHints;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import at.andiwand.library.component.JFrameUtil;
import at.netcrawler.component.CrapGraphLayout;
import at.netcrawler.component.TopologyViewer;
import at.netcrawler.network.Capability;
import at.netcrawler.network.model.NetworkCable;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.topology.HashTopology;
import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyCable;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.network.topology.TopologyInterface;


public class CrapGraphLayoutTest {
	
	public static void main(String[] args) throws Throwable {
		Topology topology = new HashTopology();
		
		TopologyViewer topologyViewer = new TopologyViewer();
		topologyViewer.setGraphLayout(new CrapGraphLayout(topologyViewer));
		topologyViewer.setModel(topology);
		topologyViewer.addRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		topologyViewer.addRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		JScrollPane scrollPane = new JScrollPane(topologyViewer);
		
		JFrame frame = new JFrame();
		frame.add(scrollPane);
		frame.setSize(400, 400);
		JFrameUtil.centerFrame(frame);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		NetworkDevice deviceA = new NetworkDevice();
		NetworkInterface interfaceA = new NetworkInterface();
		interfaceA.setValue(NetworkInterface.NAME, "eth0");
		deviceA.setValue(NetworkDevice.HOSTNAME, "RouterA");
		deviceA.setValue(NetworkDevice.INTERFACES,
				new HashSet<NetworkInterface>(Arrays.asList(interfaceA)));
		
		NetworkDevice deviceB = new NetworkDevice();
		NetworkInterface interfaceB = new NetworkInterface();
		interfaceB.setValue(NetworkInterface.NAME, "eth0");
		deviceB.setValue(NetworkDevice.HOSTNAME, "RouterB");
		deviceB.setValue(NetworkDevice.INTERFACES,
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
		
		Thread.sleep(2000);
		deviceA.setValue(NetworkDevice.MAJOR_CAPABILITY, Capability.ROUTER);
		
		Thread.sleep(2000);
		deviceB.setValue(NetworkDevice.MAJOR_CAPABILITY, Capability.SWITCH);
	}
	
}