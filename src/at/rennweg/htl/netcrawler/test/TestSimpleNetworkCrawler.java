package at.rennweg.htl.netcrawler.test;

import java.awt.BorderLayout;
import java.net.Inet4Address;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import at.andiwand.library.util.JFrameUtil;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;
import at.rennweg.htl.netcrawler.cli.executor.factory.SimpleCiscoRemoteExecutorFactory;
import at.rennweg.htl.netcrawler.cli.executor.factory.SimpleCiscoSSHExecutorFactory;
import at.rennweg.htl.netcrawler.graphics.graph.JNetworkGraph;
import at.rennweg.htl.netcrawler.network.crawler.SimpleCiscoNetworkCrawler;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;


public class TestSimpleNetworkCrawler {
	
	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		
		String rootHost = JOptionPane.showInputDialog("type in the root device", "192.168.0.254");
		if (rootHost == null) System.exit(0);
		final Inet4Address rootAddress = (Inet4Address) Inet4Address.getByName(rootHost);
		
		
		final NetworkGraph networkGraph = new NetworkGraph();
		
		
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
		
		
		SimpleCiscoUser masterUser = new SimpleCiscoUser("cisco", "cisco");
		SimpleCiscoRemoteExecutorFactory executorFactory = new SimpleCiscoSSHExecutorFactory();
		SimpleCiscoNetworkCrawler networkCrawler = new SimpleCiscoNetworkCrawler(executorFactory, masterUser, rootAddress);
		networkCrawler.crawl(networkGraph);
	}
	
}