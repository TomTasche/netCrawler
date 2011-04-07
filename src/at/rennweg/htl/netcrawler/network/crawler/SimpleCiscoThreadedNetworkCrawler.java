package at.rennweg.htl.netcrawler.network.crawler;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

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
import at.rennweg.htl.netcrawler.network.graph.NetworkInterface;
import at.rennweg.htl.netcrawler.network.graph.NetworkLink;
import at.rennweg.htl.netcrawler.network.graph.SerialLink;


public class SimpleCiscoThreadedNetworkCrawler extends NetworkCrawler {
	
	private SimpleCLIFactroy cliFactroy;
	private SimpleCiscoUser masterUser;
	private InetAddress root;
	
	private Executor executor;
	
	
	public SimpleCiscoThreadedNetworkCrawler(SimpleCLIFactroy cliFactroy, SimpleCiscoUser masterUser, InetAddress root, Executor executor) {
		this.cliFactroy = cliFactroy;
		this.masterUser = masterUser;
		this.root = root;
		
		this.executor = executor;
	}
	
	
	@Override
	public void crawl(NetworkGraph networkGraph) {
		DeviceCrawler rootCrawler = new DeviceCrawler(networkGraph, root, null);
		executor.execute(rootCrawler);
	}
	
	
	private class DeviceCrawler implements Runnable {
		private NetworkGraph networkGraph;
		private InetAddress host;
		private SimpleNeighbor sourceNeighbor;
		
		public DeviceCrawler(NetworkGraph networkGraph, InetAddress host, SimpleNeighbor sourceNeighbor) {
			this.networkGraph = networkGraph;
			this.host = host;
			this.sourceNeighbor = sourceNeighbor;
		}
		
		public void run() {
			try {
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
				
				for (SimpleNeighbor neighbor : neighbors) {
					if (neighbor.getManagementAddresses().isEmpty()) continue;
					
					InetAddress managementAddress = neighbor.getManagementAddresses().get(0);
					
					neighbor.setSourceDevice(device);
					DeviceCrawler crawler = new DeviceCrawler(networkGraph, managementAddress, neighbor);
					SimpleCiscoThreadedNetworkCrawler.this.executor.execute(crawler);
				}
				
				ciscoCli.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}