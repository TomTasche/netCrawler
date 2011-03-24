package at.rennweg.htl.netcrawler.test;

import java.awt.BorderLayout;
import java.net.Inet4Address;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import at.andiwand.library.network.ssh.SSH1Client;
import at.andiwand.library.util.JFrameUtil;
import at.andiwand.library.util.cli.CommandLine;
import at.rennweg.htl.netcrawler.cli.CiscoUser;
import at.rennweg.htl.netcrawler.graphics.graph.JNetworkGraph;
import at.rennweg.htl.netcrawler.network.crawler.SimpleCLIFactroy;
import at.rennweg.htl.netcrawler.network.crawler.SimpleCiscoNetworkCrawler;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;


public class TestSimpleNetworkCrawler {
	
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
		
		
		final CiscoUser masterUser = new CiscoUser("cisco", "cisco");
		SimpleCiscoNetworkCrawler networkCrawler = new SimpleCiscoNetworkCrawler(new SimpleCLIFactroy() {
			public CommandLine getCommandLine(InetAddress address) {
				try {
					return new SSH1Client(address, masterUser.getUsername(), masterUser.getPassword());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return null;
			}
		}, null, rootAddress);
		networkCrawler.crawl(networkGraph);
	}
	
}