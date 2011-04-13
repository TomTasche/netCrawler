package at.rennweg.htl.netcrawler.test;

import java.awt.BorderLayout;
import java.net.Inet4Address;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import at.andiwand.library.graphics.graph.RingGraphLayout;
import at.andiwand.library.util.JFrameUtil;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;
import at.rennweg.htl.netcrawler.cli.executor.factory.SimpleCiscoRemoteExecutorFactory;
import at.rennweg.htl.netcrawler.cli.executor.factory.SimplePacketTracerTelnetExecutorFactory;
import at.rennweg.htl.netcrawler.graphics.graph.JNetworkGraph;
import at.rennweg.htl.netcrawler.network.crawler.SimpleCiscoNetworkCrawler;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;


public class TestSimplePTNetworkCrawler {
	
	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		
		String rootHost = JOptionPane.showInputDialog("type in the root device", "192.168.0.254");
		if (rootHost == null) System.exit(0);
		Inet4Address rootAddress = (Inet4Address) Inet4Address.getByName(rootHost);
		
		
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		
		JNetworkGraph jNetworkGraph = new JNetworkGraph();
		jNetworkGraph.setGraphLayout(new RingGraphLayout(jNetworkGraph));
		jNetworkGraph.setAntialiasing(true);
		jNetworkGraph.setMagneticLines(true);
		JScrollPane scrollPane = new JScrollPane(jNetworkGraph);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		frame.add(scrollPane);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		JFrameUtil.centerFrame(frame);
		frame.setVisible(true);
		
		
		SimpleCiscoUser masterUser = new SimpleCiscoUser("cisco", "cisco");
		Inet4Address deviceAddress = (Inet4Address) Inet4Address.getByName("192.168.0.153");
		Inet4Address deviceGateway = (Inet4Address) Inet4Address.getByName("192.168.0.254");
		SimpleCiscoRemoteExecutorFactory executorFactory = new SimplePacketTracerTelnetExecutorFactory(deviceAddress, deviceGateway);
		SimpleCiscoNetworkCrawler networkCrawler = new SimpleCiscoNetworkCrawler(executorFactory, masterUser, rootAddress);
		
		NetworkGraph networkGraph = new NetworkGraph();
		jNetworkGraph.setGraph(networkGraph);
		networkCrawler.crawl(networkGraph);
	}
	
}