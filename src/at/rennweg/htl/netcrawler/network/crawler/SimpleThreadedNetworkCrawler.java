package at.rennweg.htl.netcrawler.network.crawler;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.andiwand.library.util.cli.CommandLine;
import at.rennweg.htl.netcrawler.cli.CiscoCommandLineExecutor;
import at.rennweg.htl.netcrawler.cli.CiscoUser;
import at.rennweg.htl.netcrawler.network.graph.CiscoDevice;
import at.rennweg.htl.netcrawler.network.graph.CiscoRouter;
import at.rennweg.htl.netcrawler.network.graph.CiscoSwitch;
import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;
import at.rennweg.htl.netcrawler.network.graph.NetworkInterface;


public class SimpleThreadedNetworkCrawler extends NetworkCrawler {
	
	private SimpleCLIFactroy cliFactroy;
	private CiscoUser masterUser;
	private InetAddress root;
	
	private Executor executor;
	
	
	public SimpleThreadedNetworkCrawler(SimpleCLIFactroy cliFactroy, CiscoUser masterUser, InetAddress root, Executor executor) {
		this.cliFactroy = cliFactroy;
		this.masterUser = masterUser;
		this.root = root;
		
		this.executor = executor;
	}
	
	
	@Override
	public void crawl(NetworkGraph networkGraph) {
		DeviceCrawler rootCrawler = new DeviceCrawler(networkGraph, root);
		executor.execute(rootCrawler);
	}
	
	
	private class DeviceCrawler implements Runnable {
		private NetworkGraph networkGraph;
		private InetAddress address;
		
		public DeviceCrawler(NetworkGraph networkGraph, InetAddress address) {
			this.networkGraph = networkGraph;
			this.address = address;
		}
		
		public void run() {
			try {
				CommandLine commandLine = cliFactroy.getCommandLine(address);
				CiscoCommandLineExecutor lineExecutor = new CiscoCommandLineExecutor(commandLine, masterUser);
				
				String runningConfig = lineExecutor.executeMore("show running-config").readWholeOutput();
				String hostname = null;
				for (String line : runningConfig.split("\n")) {
					line = line.trim();
					
					if (line.startsWith("hostname ")) {
						hostname = line.substring("hostname ".length());
						break;
					}
				}
				
				String version = lineExecutor.executeMore("show version").readWholeOutput();
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
				
				InetAddress managementAddress = address;
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
					
					commandLine.close();
					return;
				}
				networkGraph.addVertex(device);
				
				String interfaces = lineExecutor.executeMore("show ip interface brief").readWholeOutput();
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
				
				String neighbors = lineExecutor.executeMore("show cdp neighbors detail").readWholeOutput();
				Inet4Address currentAddress = null;
				for (String line : neighbors.split("\n")) {
					line = line.trim();
					
					if (line.startsWith("IP address : ")) {
						currentAddress = (Inet4Address) Inet4Address.getByName(line.substring("IP address : ".length()));
						System.out.println(hostname + " do lookup1 " + currentAddress.getHostAddress());
						DeviceCrawler neighbourCrawler = new DeviceCrawler(networkGraph, currentAddress);
						executor.execute(neighbourCrawler);
					}
				}
				
				commandLine.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}