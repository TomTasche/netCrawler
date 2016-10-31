package at.netcrawler.network.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.Connection;
import at.netcrawler.network.connection.ConnectionGateway;
import at.netcrawler.network.connection.ConnectionSettings;
import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.manager.DeviceManagerFactory;
import at.netcrawler.network.model.NetworkCable;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyCable;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.network.topology.TopologyInterface;
import at.netcrawler.network.topology.UnknownTopologyInterface;


public class SimpleNetworkCrawler implements NetworkCrawler {
	
	private ConnectionGateway gateway;
	private ConnectionSettings settings;
	private DeviceManagerFactory managerFactory;
	private IPv4Address start;
	
	public SimpleNetworkCrawler(ConnectionGateway gateway,
			ConnectionSettings settings, DeviceManagerFactory managerFactory,
			IPv4Address start) {
		this.gateway = gateway;
		this.settings = settings.clone();
		this.managerFactory = managerFactory;
		this.start = start;
	}
	
	public void crawl(Topology topology) throws IOException {
		crawlImpl(topology, start, null);
	}
	
	private void crawlImpl(Topology topology, IPv4Address address,
			TopologyDevice lastDevice) throws IOException {
		IPDeviceAccessor accessor = new IPDeviceAccessor(address);
		Connection connection = gateway.openConnection(accessor, settings);
		NetworkDevice networkDevice = new NetworkDevice();
		DeviceManager deviceManager = managerFactory.buildDeviceManager(
				networkDevice, connection);
		deviceManager.fetch();
		connection.close();
		
		TopologyDevice topologyDevice = new TopologyDevice(networkDevice);
		
		boolean success = topology.addVertex(topologyDevice);
		if (!success) {
			topologyDevice = topology.getByIdentifier(topologyDevice
					.getIdentifier());
		}
		
		if (lastDevice != null) {
			TopologyInterface interfaceA = new UnknownTopologyInterface();
			TopologyInterface interfaceB = new UnknownTopologyInterface();
			lastDevice.addInterface(interfaceA);
			topologyDevice.addInterface(interfaceB);
			NetworkCable networkCable = new NetworkCable();
			Set<TopologyInterface> connectedInterfaces = new HashSet<TopologyInterface>();
			connectedInterfaces.add(interfaceA);
			connectedInterfaces.add(interfaceB);
			TopologyCable topologyCable = new TopologyCable(networkCable,
					connectedInterfaces);
			topology.addEdge(topologyCable);
		}
		
		if (success) {
			Set<IPv4Address> neighbors = deviceManager.discoverNeighbors();
			
			for (IPv4Address neighbor : neighbors) {
				crawlImpl(topology, neighbor, topologyDevice);
			}
		}
	}
	
}