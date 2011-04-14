package at.rennweg.htl.netcrawler.network.crawler;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;
import at.rennweg.htl.netcrawler.cli.SimpleNeighbor;
import at.rennweg.htl.netcrawler.cli.executor.SimpleRemoteExecutor;
import at.rennweg.htl.netcrawler.cli.executor.factory.SimpleCiscoRemoteExecutorFactory;
import at.rennweg.htl.netcrawler.network.agent.DefaultCiscoDeviceAgent;
import at.rennweg.htl.netcrawler.network.graph.CiscoDevice;
import at.rennweg.htl.netcrawler.network.graph.CiscoSwitch;
import at.rennweg.htl.netcrawler.network.graph.EthernetLink;
import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;
import at.rennweg.htl.netcrawler.network.graph.NetworkHub;
import at.rennweg.htl.netcrawler.network.graph.NetworkInterface;
import at.rennweg.htl.netcrawler.network.graph.NetworkLink;
import at.rennweg.htl.netcrawler.network.graph.SerialLink;


public class SimpleCiscoThreadedNetworkCrawler extends NetworkCrawler {
	
	private SimpleCiscoRemoteExecutorFactory executorFactory;
	private SimpleCiscoUser masterUser;
	private InetAddress root;
	
	private ExecutorService executor;
	private int open;
	private Object syncOpen = new Object();
	private Object sync = new Object();
	
	
	public SimpleCiscoThreadedNetworkCrawler(SimpleCiscoRemoteExecutorFactory executorFactory, SimpleCiscoUser masterUser, InetAddress root, ExecutorService executor) {
		this.executorFactory = executorFactory;
		this.masterUser = masterUser;
		this.root = root;
		
		this.executor = executor;
	}
	
	
	@Override
	public void crawl(NetworkGraph networkGraph) throws InterruptedException {
		DeviceCrawler rootCrawler = new DeviceCrawler(networkGraph, root, null);
		executor.execute(rootCrawler);
		
		synchronized (sync) {
			sync.wait();
		}
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
				SimpleRemoteExecutor executor = executorFactory.getRemoteExecutor(host, masterUser);
				DefaultCiscoDeviceAgent deviceAgent = new DefaultCiscoDeviceAgent(executor);
				
				CiscoDevice device = deviceAgent.fetchComparable();
				
				if (networkGraph.getVertices().contains(device)) {
					HashSet<NetworkDevice> dummy = new HashSet<NetworkDevice>();
					dummy.add(device);
					Set<NetworkDevice> singleDevice = new HashSet<NetworkDevice>(networkGraph.getVertices());
					singleDevice.retainAll(dummy);
					device = (CiscoDevice) new ArrayList<NetworkDevice>(singleDevice).get(0);
					
					executor.close();
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
						
						if (otherDevice instanceof NetworkHub) {
							ethernetLink.setCrossover(CiscoSwitch.class.isInstance(device));
						} else {
							ethernetLink.setCrossover(device.getClass().equals(otherDevice.getClass()));
						}
						
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
						ethernetLink.setCrossover(CiscoSwitch.class.isInstance(device));
						
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
					DeviceCrawler crawler = new DeviceCrawler(networkGraph, managementAddress, neighbor);
					
					synchronized (syncOpen) {
						open++;
					}
					
					SimpleCiscoThreadedNetworkCrawler.this.executor.execute(crawler);
				}
				
				executor.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				synchronized (syncOpen) {
					if (open == 0) {
						synchronized (sync) {
							sync.notify();
						}
						
						return;
					}
					
					open--;
				}
			}
		}
	}
	
}