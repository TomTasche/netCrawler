package at.rennweg.htl.netcrawler.test;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import at.andiwand.library.network.MACAddress;
import at.andiwand.library.util.JFrameUtil;
import at.andiwand.library.util.cli.CommandLine;
import at.andiwand.packettracer.ptmp.simple.SimpleMultiuserClient;
import at.andiwand.packettracer.ptmp.simple.SimpleNetworkDevice;
import at.andiwand.packettracer.ptmp.simple.SimpleNetworkDeviceFactory;
import at.rennweg.htl.netcrawler.cli.CiscoUser;
import at.rennweg.htl.netcrawler.graphics.graph.JNetworkGraph;
import at.rennweg.htl.netcrawler.network.crawler.SimpleCLIFactroy;
import at.rennweg.htl.netcrawler.network.crawler.SimpleThreadedNetworkCrawler;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;


public class TestThreadedSimpleThreadedNetworkCrawler {
	
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
		jNetworkGraph.setMagneticLines(true);
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
		
		
		CiscoUser masterUser = new CiscoUser("cisco", "cisco");
		SimpleThreadedNetworkCrawler networkCrawler = new SimpleThreadedNetworkCrawler(new SimpleCLIFactroy() {
			public CommandLine getCommandLine(InetAddress address) {
				try {
					return networkDevice.createTelnetConnection((Inet4Address) address);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return null;
			}
		}, masterUser, rootAddress, Executors.newFixedThreadPool(10));
		networkCrawler.crawl(networkGraph);
	}
	
}