package at.rennweg.htl.netcrawler.test;

import java.awt.BorderLayout;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import at.andiwand.library.network.MACAddress;
import at.andiwand.library.util.JFrameUtil;
import at.andiwand.packettracer.ptmp.simple2.SimpleMultiuserClient;
import at.andiwand.packettracer.ptmp.simple2.SimpleNetworkDevice;
import at.andiwand.packettracer.ptmp.simple2.SimpleNetworkDeviceFactory;
import at.andiwand.packettracer.ptmp.simple2.SimpleTelnetConnection;
import at.rennweg.htl.netcrawler.cli.CiscoCommandLineExecutor;
import at.rennweg.htl.netcrawler.cli.CiscoUser;
import at.rennweg.htl.netcrawler.graphics.graph.JNetworkGraph;
import at.rennweg.htl.netcrawler.network.graph.CiscoDevice;
import at.rennweg.htl.netcrawler.network.graph.CiscoRouter;
import at.rennweg.htl.netcrawler.network.graph.CiscoSwitch;
import at.rennweg.htl.netcrawler.network.graph.EthernetCable;
import at.rennweg.htl.netcrawler.network.graph.NetworkCable;
import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;
import at.rennweg.htl.netcrawler.network.graph.NetworkInterface;
import at.rennweg.htl.netcrawler.network.graph.SerialCable;

import com.jcraft.jsch.JSchException;


public class TestExplorePTNetwork {
	
	private static SimpleNetworkDevice networkDevice;
	private static final Object networkDeviceSync = new Object();
	
	public static void main(String[] args) throws Throwable {
		String rootHost = JOptionPane.showInputDialog("type in the root device", "192.168.0.254");
		if (rootHost == null) System.exit(0);
		final Inet4Address rootAddress = (Inet4Address) Inet4Address.getByName(rootHost);
		
		
		final NetworkGraph networkGraph = new NetworkGraph();
		
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		
		JNetworkGraph jNetworkGraph = new JNetworkGraph();
		jNetworkGraph.setGraph(networkGraph);
		jNetworkGraph.setAntialiasing(true);
		jNetworkGraph.setMagneticRaster(true);
		JScrollPane scrollPane = new JScrollPane(jNetworkGraph);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		frame.add(scrollPane);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		JFrameUtil.centerFrame(frame);
		frame.setVisible(true);
		
		
		SimpleMultiuserClient multiuserClient = new SimpleMultiuserClient();
		multiuserClient.setInterfaceFactory(new SimpleNetworkDeviceFactory() {
			public SimpleNetworkDevice createInterface(String[] linkRequest) {
				if (networkDevice != null) return null;
				
				try {
					String name = "Simple Bridge Interface";
					MACAddress macAddress = new MACAddress("00:24:8c:fd:fe:96");
					Inet4Address inet4Address = (Inet4Address) Inet4Address.getByName("192.168.0.1");
					Inet4Address defaultRoute = (Inet4Address) Inet4Address.getByName("192.168.0.254");
					
					SimpleNetworkDevice device = new SimpleNetworkDevice(name, macAddress, inet4Address);
					device.setDefaultRoute(defaultRoute);
					
					synchronized (networkDeviceSync) {
						networkDevice = device;
						networkDeviceSync.notify();
					}
					
					return device;
				} catch (UnknownHostException e) {}
				
				return null;
			}
		});
		multiuserClient.connect(InetAddress.getLocalHost());
		
		synchronized (networkDeviceSync) {
			if (networkDevice == null) networkDeviceSync.wait();
		}
		
		
		new Thread() {
			public void run() {
				try {
					recursiveLookup(networkGraph, rootAddress);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	
	public static final CiscoUser USER = new CiscoUser("cisco", "cisco");
	
	public static CiscoDevice recursiveLookup(NetworkGraph networkGraph, Inet4Address host) throws Exception {
		SimpleTelnetConnection ciscoCli = networkDevice.createTelnetConnection(host);
		ciscoCli.connect();
		CiscoCommandLineExecutor executor = new CiscoCommandLineExecutor(ciscoCli, USER);
		
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
			
			try {
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
						
						NetworkCable cable = null;
						
						if (deviceInterface.getName().toLowerCase().contains("ethernet")) {
							EthernetCable ethernetCable = new EthernetCable(deviceInterface, neighbourInterface);
							ethernetCable.setCrossover(device.getClass().equals(otherDevice.getClass()));
							
							cable = ethernetCable;
						} else if (deviceInterface.getName().toLowerCase().contains("serial")) {
							SerialCable serialCable = new SerialCable(deviceInterface, neighbourInterface);
							
							cable = serialCable;
						}
						
						if (!networkGraph.getEdges().contains(cable))
							networkGraph.addEdge(cable);
					}
				}
			} catch (JSchException e) {}
		}
		if (currentAddress != null) {
			System.out.println(hostname + " do lookup2 " + currentAddress.getHostAddress());
			CiscoDevice otherDevice = recursiveLookup(networkGraph, currentAddress);
			
			NetworkInterface deviceInterface = device.getInterface(currentInterface);
			NetworkInterface neighbourInterface = otherDevice.getInterface(currentNeighbourInterface);
			
			NetworkCable cable = null;
			
			if (deviceInterface.getName().toLowerCase().contains("ethernet")) {
				EthernetCable ethernetCable = new EthernetCable(deviceInterface, neighbourInterface);
				ethernetCable.setCrossover(device.getClass().equals(otherDevice.getClass()));
				
				cable = ethernetCable;
			} else if (deviceInterface.getName().toLowerCase().contains("serial")) {
				SerialCable serialCable = new SerialCable(deviceInterface, neighbourInterface);
				
				cable = serialCable;
			}
			
			if (!networkGraph.getEdges().contains(cable))
				networkGraph.addEdge(cable);
		}
		
		ciscoCli.close();
		return device;
	}
	
}