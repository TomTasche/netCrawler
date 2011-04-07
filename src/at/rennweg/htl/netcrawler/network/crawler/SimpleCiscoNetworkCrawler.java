package at.rennweg.htl.netcrawler.network.crawler;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.andiwand.library.util.cli.CommandLine;
import at.rennweg.htl.netcrawler.cli.SimpleCachedCommandLineExecutor;
import at.rennweg.htl.netcrawler.cli.SimpleNeighbor;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoCommandLineExecutor;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;
import at.rennweg.htl.netcrawler.cli.factory.SimpleCLIFactroy;
import at.rennweg.htl.netcrawler.network.agent.DefaultCiscoDeviceAgent;
import at.rennweg.htl.netcrawler.network.graph.CiscoDevice;
import at.rennweg.htl.netcrawler.network.graph.EthernetLink;
import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;
import at.rennweg.htl.netcrawler.network.graph.NetworkHub;
import at.rennweg.htl.netcrawler.network.graph.NetworkInterface;
import at.rennweg.htl.netcrawler.network.graph.NetworkLink;
import at.rennweg.htl.netcrawler.network.graph.SerialLink;


public class SimpleCiscoNetworkCrawler extends NetworkCrawler {
	
	private SimpleCLIFactroy cliFactroy;
	private SimpleCiscoUser masterUser;
	private InetAddress root;
	
	public SimpleCiscoNetworkCrawler(SimpleCLIFactroy cliFactroy, InetAddress root) {
		this(cliFactroy, null, root);
	}
	public SimpleCiscoNetworkCrawler(SimpleCLIFactroy cliFactroy, SimpleCiscoUser masterUser, InetAddress root) {
		this.cliFactroy = cliFactroy;
		this.masterUser = masterUser;
		this.root = root;
	}
	
	@Override
	public void crawl(NetworkGraph networkGraph) throws Exception {
		recursiveLookup(networkGraph, root, null);
	}
	
	private void recursiveLookup(NetworkGraph networkGraph, InetAddress host, SimpleNeighbor sourceNeighbor) throws Exception {
		CommandLine ciscoCli = cliFactroy.getCommandLine(host, masterUser);
		SimpleCiscoCommandLineExecutor executor = new SimpleCiscoCommandLineExecutor(ciscoCli);
		SimpleCachedCommandLineExecutor cachedExecutor = new SimpleCachedCommandLineExecutor(executor);
		DefaultCiscoDeviceAgent deviceAgent = new DefaultCiscoDeviceAgent(cachedExecutor);
		
		CiscoDevice device = deviceAgent.fetchComparable();
		
		if (networkGraph.getVertices().contains(device)) {
			HashSet<NetworkDevice> dummy = new HashSet<NetworkDevice>();
			dummy.add(device);
			Set<NetworkDevice> singleDevice = new HashSet<NetworkDevice>(networkGraph.getVertices());
			singleDevice.retainAll(dummy);
			device = (CiscoDevice) new ArrayList<NetworkDevice>(singleDevice).get(0);
			
			ciscoCli.close();
			return;
		}
		
		deviceAgent.fetchAll(device);
		networkGraph.addVertex(device);
		
		if (sourceNeighbor != null) {
			NetworkDevice otherDevice = sourceNeighbor.getSourceDevice();
			
			NetworkInterface deviceInterface = device.getInterface(sourceNeighbor.getInterfaceName());
			NetworkInterface neighbourInterface = otherDevice.getInterface(sourceNeighbor.getSourceInterfaceName());
			
			NetworkLink link = null;
			
			if (deviceInterface.getName().toLowerCase().contains("ethernet")) {
				EthernetLink ethernetLink = new EthernetLink(deviceInterface, neighbourInterface);
				ethernetLink.setCrossover(device.getClass().equals(otherDevice.getClass()));
				
				link = ethernetLink;
			} else if (deviceInterface.getName().toLowerCase().contains("serial")) {
				SerialLink serialLink = new SerialLink(deviceInterface, neighbourInterface);
				
				link = serialLink;
			}
			
			if (link != null) networkGraph.addEdge(link);
		}
		
		List<SimpleNeighbor> neighbors = deviceAgent.fetchNeighbors();
		
		Map<String, NetworkHub> hubs = new HashMap<String, NetworkHub>();
		Map<String, Integer> hubPorts = new HashMap<String, Integer>();
		List<SimpleNeighbor> hubsNeighbors = new ArrayList<SimpleNeighbor>(neighbors);
		
		if (sourceNeighbor == null || !(sourceNeighbor.getSourceDevice() instanceof NetworkHub)) {
			while (!hubsNeighbors.isEmpty()) {
				SimpleNeighbor firstNeighbor = hubsNeighbors.get(0);
				List<SimpleNeighbor> hubNeighbors = new ArrayList<SimpleNeighbor>();
				hubNeighbors.add(firstNeighbor);
				Set<NetworkInterface> hubInterfaces = new HashSet<NetworkInterface>();
				hubInterfaces.add(new NetworkInterface("Port0"));
				hubInterfaces.add(new NetworkInterface("Port1"));
				
				int count;
				for (count = 1; count < hubsNeighbors.size(); count++) {
					SimpleNeighbor neighbor = hubsNeighbors.get(count);
					
					if (firstNeighbor.getSourceInterfaceName().equals(neighbor.getSourceInterfaceName())) {
						hubNeighbors.add(neighbor);
						hubInterfaces.add(new NetworkInterface("Port" + (count + 1)));
					}
				}
				
				if (hubNeighbors.size() >= 2) {
					NetworkHub hub = new NetworkHub();
					networkGraph.addVertex(hub);
					hubs.put(firstNeighbor.getSourceInterfaceName(), hub);
					hubPorts.put(firstNeighbor.getSourceInterfaceName(), 1);
					hub.setInterfaces(hubInterfaces);
				}
				
				hubsNeighbors.removeAll(hubNeighbors);
			}
			
			for (Map.Entry<String, NetworkHub> hubPort : hubs.entrySet()) {
				String interfaceName = hubPort.getKey();
				NetworkHub hub = hubPort.getValue();
				
				NetworkInterface a = device.getInterface(interfaceName);
				NetworkInterface b = hub.getInterface("Port0");
				
				EthernetLink ethernetLink = new EthernetLink(a, b);
				
				networkGraph.addEdge(ethernetLink);
			}
		}
		
		for (SimpleNeighbor neighbor : neighbors) {
			if (neighbor.getManagementAddresses().isEmpty()) continue;
			if ((sourceNeighbor != null) && sourceNeighbor.getInterfaceName().equals(neighbor.getSourceInterfaceName()))
				continue;
			
			if (hubs.containsKey(neighbor.getSourceInterfaceName())) {
				NetworkHub hub = hubs.get(neighbor.getSourceInterfaceName());
				int port = hubPorts.get(neighbor.getSourceInterfaceName());
				hubPorts.put(neighbor.getSourceInterfaceName(), port + 1);
				
				neighbor.setSourceDevice(hub);
				neighbor.setSourceInterfaceName("Port" + port);
			} else {
				neighbor.setSourceDevice(device);
			}
			
			InetAddress managementAddress = neighbor.getManagementAddresses().get(0);
			recursiveLookup(networkGraph, managementAddress, neighbor);
		}
		
		ciscoCli.close();
	}
	
}