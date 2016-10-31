package at.netcrawler.test;

import java.util.Arrays;
import java.util.HashSet;

import at.netcrawler.io.json.JsonHelper;
import at.netcrawler.network.model.NetworkCable;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.model.information.identifier.UniqueDeviceIdentifier;
import at.netcrawler.network.topology.HashTopology;
import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyCable;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.network.topology.TopologyInterface;
import at.netcrawler.ui.component.TopologyViewer;


public class HashTopologyTest {
	
	public static void main(String[] args) {
		Topology topology = new HashTopology();
		
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
		
		TopologyDevice topologyDeviceA = new TopologyDevice(
				new UniqueDeviceIdentifier(), deviceA);
		TopologyInterface topologyInterfaceA = new TopologyInterface(interfaceA);
		topologyDeviceA.addInterface(topologyInterfaceA);
		TopologyDevice topologyDeviceB = new TopologyDevice(
				new UniqueDeviceIdentifier(), deviceB);
		TopologyInterface topologyInterfaceB = new TopologyInterface(interfaceB);
		topologyDeviceB.addInterface(topologyInterfaceB);
		TopologyCable topologyCable = new TopologyCable(cable,
				new HashSet<TopologyInterface>(Arrays.asList(
						topologyInterfaceA, topologyInterfaceB)));
		
		topology.addVertex(topologyDeviceA);
		topology.addVertex(topologyDeviceB);
		topology.addEdge(topologyCable);
		
		System.out.println(JsonHelper.getGson().toJson(topology));
		System.out.println(JsonHelper.getGson().toJson(
				new TopologyViewer(topology)));
	}
	
}