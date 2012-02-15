package at.netcrawler.gui.main;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import at.netcrawler.network.Capability;
import at.netcrawler.network.model.NetworkCable;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.topology.HashTopology;
import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyCable;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.network.topology.TopologyInterface;

public class Blabla {
	
	public static void list(Topology topology) {
		for (TopologyDevice topologyDevice : topology.getVertices()) {
			NetworkDevice device = topologyDevice.getNetworkDevice();
			
			show(device);
		}
	}
	
	public static void show(NetworkDevice device) {
		String name = (String) device.getValue(NetworkDevice.HOSTNAME);
		String caps = "";
		
		Set<Capability> capabilities = (Set<Capability>) device.getValue(NetworkDevice.CAPABILITIES);
		for (Capability capability : capabilities) {
			caps += capability.name().substring(0, 1);
		}
		
		System.out.println(name + "(" + caps + ")");
	}
	
	public static void main(String[] args) {
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
		
		list(topology);
	}
}
