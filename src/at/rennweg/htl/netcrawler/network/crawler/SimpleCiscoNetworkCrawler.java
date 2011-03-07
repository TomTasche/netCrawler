package at.rennweg.htl.netcrawler.network.crawler;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.andiwand.library.util.cli.CommandLine;
import at.rennweg.htl.netcrawler.cli.CiscoCommandLineExecutor;
import at.rennweg.htl.netcrawler.cli.CiscoUser;
import at.rennweg.htl.netcrawler.network.graph.CiscoDevice;
import at.rennweg.htl.netcrawler.network.graph.CiscoRouter;
import at.rennweg.htl.netcrawler.network.graph.CiscoSwitch;
import at.rennweg.htl.netcrawler.network.graph.EthernetLink;
import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;
import at.rennweg.htl.netcrawler.network.graph.NetworkInterface;
import at.rennweg.htl.netcrawler.network.graph.NetworkLink;
import at.rennweg.htl.netcrawler.network.graph.SerialLink;


public class SimpleCiscoNetworkCrawler extends NetworkCrawler {
	
	private SimpleCLIFactroy cliFactroy;
	private CiscoUser masterUser;
	private InetAddress root;
	
	
	public SimpleCiscoNetworkCrawler(SimpleCLIFactroy cliFactroy, CiscoUser masterUser, InetAddress root) {
		this.cliFactroy = cliFactroy;
		this.masterUser = masterUser;
		this.root = root;
	}
	
	
	@Override
	public void crawl(NetworkGraph networkGraph) throws IOException {
		recursiveLookup(networkGraph, (Inet4Address) root);
	}
	
	
	private CiscoDevice recursiveLookup(NetworkGraph networkGraph, Inet4Address host) throws IOException {
		CommandLine ciscoCli = cliFactroy.getCommandLine(host);
		CiscoCommandLineExecutor executor = new CiscoCommandLineExecutor(ciscoCli, masterUser);
		
		String runningConfig = executor.executeMore("show running-config").readWholeOutput();
		String hostname = null;
		for (String line : runningConfig.split("\n")) {
			line = line.trim();
			
			if (line.startsWith("hostname ")) {
				hostname = line.substring("hostname ".length());
				break;
			}
		}
		
		String version = executor.executeMore("show version").readWholeOutput();
		Pattern seriesNumberPattern = Pattern.compile("^.* (.*?) Software \\((.*?)\\).*$");
		String seriesNumber = null;
		String deviceId = null;
		for (String line : version.split("\n")) {
			line = line.trim();
			
			Matcher seriesNumberMatcher = seriesNumberPattern.matcher(line);
			if (seriesNumberMatcher.matches()) {
				seriesNumber = seriesNumberMatcher.group(1);
			} else if (line.startsWith("Processor board ID ")) {
				deviceId = line.substring("Processor board ID ".length());
			}
		}
		
		CiscoDevice device = null;
		
		if (seriesNumber.startsWith("28")) device = new CiscoRouter();
		else if (seriesNumber.startsWith("C3560")) device = new CiscoSwitch();
		else device = new CiscoDevice();
		
		InetAddress managementAddress = host;
		Set<InetAddress> managementAddresses = new HashSet<InetAddress>();
		managementAddresses.add(managementAddress);
		device.setManagementAddresses(managementAddresses);
		device.setHostname(hostname);
		device.setSeriesNumber(seriesNumber);
		device.setProcessorBoardId(deviceId);
		
		System.out.println(hostname);
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
		
		String interfaces = executor.executeMore("show ip interface brief").readWholeOutput();
		for (String line : interfaces.split("\n")) {
			if (line.toLowerCase().startsWith("interface")) continue;
			
			String[] columns = line.split("\\s+");
			
			if (columns.length != 6) continue;
			if (columns[4].toLowerCase().equals("down")) continue;
			if (columns[5].toLowerCase().equals("down")) continue;
			
			NetworkInterface networkInterface = new NetworkInterface(columns[0]);
			device.addInterface(networkInterface);
		}
		System.out.println(device.getInterfaces());
		
		String neighbors = executor.executeMore("show cdp neighbors detail").readWholeOutput();
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
					System.out.println(hostname + " do lookup1 " + currentAddress.getHostAddress());
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
			System.out.println(hostname + " do lookup2 " + currentAddress.getHostAddress());
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