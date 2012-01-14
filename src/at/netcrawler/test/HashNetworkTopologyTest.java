package at.netcrawler.test;

import java.util.Arrays;
import java.util.HashSet;

import at.netcrawler.network.model.NetworkCable;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.topology.HashNetworkTopology;
import at.netcrawler.network.topology.NetworkTopology;
import at.netcrawler.network.topology.TopologyCable;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.network.topology.TopologyInterface;


public class HashNetworkTopologyTest {
	
	public static void main(String[] args) {
		NetworkTopology networkTopology = new HashNetworkTopology();
		
		NetworkDevice deviceA = new NetworkDevice();
		NetworkInterface interfaceA = new NetworkInterface();
		interfaceA.setValue(NetworkInterface.NAME, "eth0");
		deviceA.setValue(NetworkDevice.IDENTICATION, "a");
		deviceA.setValue(NetworkDevice.HOSTNAME, "RouterA");
		deviceA.setValue(NetworkDevice.INTERFACES,
				new HashSet<NetworkInterface>(Arrays.asList(interfaceA)));
		
		NetworkDevice deviceB = new NetworkDevice();
		NetworkInterface interfaceB = new NetworkInterface();
		interfaceB.setValue(NetworkInterface.NAME, "eth0");
		deviceB.setValue(NetworkDevice.IDENTICATION, "b");
		deviceB.setValue(NetworkDevice.HOSTNAME, "RouterB");
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
		
		networkTopology.addDevice(topologyDeviceA);
		networkTopology.addDevice(topologyDeviceB);
		networkTopology.addCable(topologyCable);
		
		System.out.println(networkTopology.getEdgeCount());
		
		deviceA.setValue(NetworkDevice.INTERFACES,
				new HashSet<NetworkInterface>());
		
		System.out.println(networkTopology.getEdgeCount());
	}
	
}