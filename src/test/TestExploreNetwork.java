package test;

import java.awt.BorderLayout;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import network.ssh.SimpleSSH1Executor;
import network.ssh.SimpleSSH2Executor;
import network.ssh.SimpleSSHExecutor;
import at.rennweg.htl.netcrawler.graphics.graph.JNetworkGraph;
import at.rennweg.htl.netcrawler.network.graph.CiscoDevice;
import at.rennweg.htl.netcrawler.network.graph.CiscoRouter;
import at.rennweg.htl.netcrawler.network.graph.CiscoSwitch;
import at.rennweg.htl.netcrawler.network.graph.EthernetCable;
import at.rennweg.htl.netcrawler.network.graph.NetworkCable;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;

import com.jcraft.jsch.JSchException;


public class TestExploreNetwork {
	
	public static void main(String[] args) throws Throwable {
		final NetworkGraph<CiscoDevice, NetworkCable<CiscoDevice>> networkGraph =
			new NetworkGraph<CiscoDevice, NetworkCable<CiscoDevice>>();
		
		
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
		
		
		final String rootHost = "192.168.0.254";
		
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
	
	public static CiscoDevice recursiveLookup(NetworkGraph<CiscoDevice, NetworkCable<CiscoDevice>> networkGraph, String host) throws Exception {
		SimpleSSHExecutor executor = null;
		try {
			System.out.println("try ssh2");
			executor = new SimpleSSH2Executor(host, USER, PASSWORD);
			executor.execute("show clock");
			System.out.println("success ssh2");
		} catch (Exception e) {
			System.out.println("try ssh1");
			executor = new SimpleSSH1Executor(host, USER, PASSWORD);
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
		String software = null;
		String deviceId = null;
		for (String line : version.split("\n")) {
			line = line.trim();
			
			if (line.startsWith("Cisco IOS Software")) {
				software = line.split(",")[1].trim().split(" ")[0];
			} else if (line.startsWith("Processor board ID ")) {
				deviceId = line.substring("Processor board ID ".length());
			}
		}
		
		CiscoDevice device = null;
		
		System.out.println(software);
		
		if (software.startsWith("28")) device = new CiscoRouter();
		else if (software.startsWith("C3560")) device = new CiscoSwitch();
		else device = new CiscoDevice();
		
		InetAddress managementAddress = InetAddress.getByName(host);
		device.setManagementAddress(managementAddress);
		device.setName(hostname);
		device.setSoftware(software);
		device.setProcessorBoardId(deviceId);
		
		System.out.println(hostname);
		if (networkGraph.getVertices().contains(device)) return device;
		networkGraph.addVertex(device);
		
		String neighbors = executor.execute("show cdp neighbors detail");
		for (String line : neighbors.split("\n")) {
			line = line.trim();
			
			if (line.startsWith("IP address: ")) {
				try {
					String address = line.substring("IP address: ".length());
					CiscoDevice otherDevice = recursiveLookup(networkGraph, address);
					
					EthernetCable<CiscoDevice> ethernetCable =
						new EthernetCable<CiscoDevice>(device, otherDevice);
					
					ethernetCable.setCrossover(device.getClass().equals(otherDevice.getClass()));
					
					if (!networkGraph.getEdges().contains(ethernetCable))
						networkGraph.addEdge(ethernetCable);
				} catch (JSchException e) {}
			}
		}
		
		return device;
	}
	
}