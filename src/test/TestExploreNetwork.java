package test;

import java.awt.BorderLayout;
import java.net.InetAddress;
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

import network.ssh.SSH1Executor;
import network.ssh.SSH2Executor;
import network.ssh.SSHExecutor;
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


public class TestExploreNetwork {
	
	public static void main(String[] args) throws Throwable {
		final String rootHost = JOptionPane.showInputDialog("type in the root device", "192.168.0.254");
		if (rootHost == null) System.exit(0);
		
		
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
		frame.setVisible(true);
		
		
		new Thread() {
			public void run() {
				try {
					recursiveLookup(networkGraph, rootHost);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	
	public static final String USER = "cisco";
	public static final String PASSWORD = "cisco";
	
	public static CiscoDevice recursiveLookup(NetworkGraph networkGraph, String host) throws Exception {
		SSHExecutor executor = null;
		try {
			System.out.println("try ssh2");
			executor = new SSH2Executor(host, USER, PASSWORD);
			executor.execute("show clock");
			System.out.println("success ssh2");
		} catch (Exception e) {
			System.out.println("try ssh1");
			executor = new SSH1Executor(host, USER, PASSWORD);
			executor.execute("show clock");
			System.out.println("success ssh1");
		}
		
		String runningConfig = executor.execute("show running-config");
		String hostname = null;
		for (String line : runningConfig.split("\n")) {
			line = line.trim();
			
			if (line.startsWith("hostname ")) {
				hostname = line.substring("hostname ".length());
				break;
			}
		}
		
		String version = executor.execute("show version");
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
		
		System.out.println(seriesNumber);
		
		if (seriesNumber.startsWith("28")) device = new CiscoRouter();
		else if (seriesNumber.startsWith("C3560")) device = new CiscoSwitch();
		else device = new CiscoDevice();
		
		InetAddress managementAddress = InetAddress.getByName(host);
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
			return device;
		}
		networkGraph.addVertex(device);
		
		String interfaces = executor.execute("show ip interface brief");
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
		
		String neighbors = executor.execute("show cdp neighbors detail");
		String currentAddress = null;
		String currentInterface = null;
		String currentNeighbourInterface = null;
		for (String line : neighbors.split("\n")) {
			line = line.trim();
			
			try {
				if (line.startsWith("Device ID: ")) {
					if (currentAddress != null) {
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
				} else if (line.startsWith("IP address: ")) {
					currentAddress = line.substring("IP address: ".length());
				} else if (line.startsWith("Interface: ")) {
					currentInterface = line.substring("Interface: ".length()).split(",")[0];
					currentNeighbourInterface = line.split(",")[1].split(":")[1].trim();
				}
			} catch (JSchException e) {}
		}
		if (currentAddress != null) {
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
		
		return device;
	}
	
}