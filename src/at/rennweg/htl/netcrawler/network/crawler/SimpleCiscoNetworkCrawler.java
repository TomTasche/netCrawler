package at.rennweg.htl.netcrawler.network.crawler;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import at.andiwand.library.util.cli.CommandLine;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoCommandLineExecutor;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;
import at.rennweg.htl.netcrawler.cli.factory.SimpleCLIFactroy;
import at.rennweg.htl.netcrawler.network.agent.DefaultCiscoDeviceAgent;
import at.rennweg.htl.netcrawler.network.graph.CiscoDevice;
import at.rennweg.htl.netcrawler.network.graph.EthernetLink;
import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;
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
		recursiveLookup(networkGraph, (Inet4Address) root);
	}
	
	private CiscoDevice recursiveLookup(NetworkGraph networkGraph, Inet4Address host) throws Exception {
		CommandLine ciscoCli = cliFactroy.getCommandLine(host, masterUser);
		SimpleCiscoCommandLineExecutor executor = new SimpleCiscoCommandLineExecutor(ciscoCli);
		DefaultCiscoDeviceAgent deviceAgent = new DefaultCiscoDeviceAgent(executor);
		
		//TODO: optimize
		CiscoDevice device = deviceAgent.fetchAll();
		
		if (networkGraph.getVertices().contains(device)) {
			HashSet<NetworkDevice> dummy = new HashSet<NetworkDevice>();
			dummy.add(device);
			Collection<NetworkDevice> singeDevice = networkGraph.getVertices();
			singeDevice.retainAll(dummy);
			device = (CiscoDevice) new ArrayList<NetworkDevice>(singeDevice).get(0);
			
			ciscoCli.close();
			return device;
		}
		networkGraph.addVertex(device);
		
		String neighbors = executor.execute("show cdp neighbors detail");
		Inet4Address currentAddress = null;
		String currentInterface = null;
		String currentNeighbourInterface = null;
		for (String line : neighbors.split("\n")) {
			line = line.trim();
			
			if (line.startsWith("IP address : ")) {
				currentAddress = (Inet4Address) Inet4Address.getByName(line.substring("IP address : ".length()));
			} else if (line.startsWith("Interface: ")) {
				currentInterface = line.substring("Interface: ".length()).split(",")[0];
				currentNeighbourInterface = line.split(",")[1].split(":")[1].trim();
				
				if (currentAddress != null) {
					CiscoDevice otherDevice = recursiveLookup(networkGraph, currentAddress);
					
					NetworkInterface deviceInterface = device.getInterface(currentInterface);
					NetworkInterface neighbourInterface = otherDevice.getInterface(currentNeighbourInterface);
					
					NetworkLink cable = null;
					
					if (deviceInterface.getName().toLowerCase().contains("ethernet")) {
						EthernetLink ethernetCable = new EthernetLink(deviceInterface, neighbourInterface);
						ethernetCable.setCrossover(device.getClass().equals(otherDevice.getClass()));
						
						cable = ethernetCable;
					} else if (deviceInterface.getName().toLowerCase().contains("serial")) {
						SerialLink serialCable = new SerialLink(deviceInterface, neighbourInterface);
						
						cable = serialCable;
					}
					
					if (!networkGraph.getEdges().contains(cable))
						networkGraph.addEdge(cable);
				}
			}
		}
		if (currentAddress != null) {
			CiscoDevice otherDevice = recursiveLookup(networkGraph, currentAddress);
			
			NetworkInterface deviceInterface = device.getInterface(currentInterface);
			NetworkInterface neighbourInterface = otherDevice.getInterface(currentNeighbourInterface);
			
			NetworkLink cable = null;
			
			if (deviceInterface.getName().toLowerCase().contains("ethernet")) {
				EthernetLink ethernetCable = new EthernetLink(deviceInterface, neighbourInterface);
				ethernetCable.setCrossover(device.getClass().equals(otherDevice.getClass()));
				
				cable = ethernetCable;
			} else if (deviceInterface.getName().toLowerCase().contains("serial")) {
				SerialLink serialCable = new SerialLink(deviceInterface, neighbourInterface);
				
				cable = serialCable;
			}
			
			if (!networkGraph.getEdges().contains(cable))
				networkGraph.addEdge(cable);
		}
		
		ciscoCli.close();
		return device;
	}
	
}