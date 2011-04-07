package at.rennweg.htl.netcrawler.test;

import java.awt.BorderLayout;
import java.net.Inet4Address;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import at.andiwand.library.graphics.graph.RingGraphLayout;
import at.andiwand.library.util.JFrameUtil;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;
import at.rennweg.htl.netcrawler.cli.factory.SimpleCLIFactroy;
import at.rennweg.htl.netcrawler.cli.factory.SimplePTTelnetFactory;
import at.rennweg.htl.netcrawler.graphics.graph.JNetworkGraph;
import at.rennweg.htl.netcrawler.network.crawler.SimpleCiscoThreadedNetworkCrawler;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;


public class TestSimplePTThreadedNetworkCrawler {
	
	public static void main(String[] args) throws Throwable {
		String rootHost = JOptionPane.showInputDialog("type in the root device", "192.168.0.254");
		if (rootHost == null) System.exit(0);
		Inet4Address rootAddress = (Inet4Address) Inet4Address.getByName(rootHost);
		
		
		NetworkGraph networkGraph = new NetworkGraph();
		
		
		//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		
		JNetworkGraph jNetworkGraph = new JNetworkGraph();
		jNetworkGraph.setGraphLayout(new RingGraphLayout(jNetworkGraph));
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
		
		
		SimpleCiscoUser masterUser = new SimpleCiscoUser("cisco", "cisco");
		Inet4Address deviceAddress = (Inet4Address) Inet4Address.getByName("192.168.0.1");
		Inet4Address deviceGateway = (Inet4Address) Inet4Address.getByName("192.168.0.254");
		SimpleCLIFactroy cliFactroy = new SimplePTTelnetFactory(deviceAddress, deviceGateway);
		SimpleCiscoThreadedNetworkCrawler networkCrawler = new SimpleCiscoThreadedNetworkCrawler(cliFactroy, masterUser, rootAddress, Executors.newFixedThreadPool(10));
		networkCrawler.crawl(networkGraph);
	}
	
}