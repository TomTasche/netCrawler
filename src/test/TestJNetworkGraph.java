package test;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import at.rennweg.htl.netcrawler.graphics.graph.JNetworkGraph;
import at.rennweg.htl.netcrawler.network.graph.CiscoRouter;
import at.rennweg.htl.netcrawler.network.graph.EthernetCable;
import at.rennweg.htl.netcrawler.network.graph.NetworkCable;
import at.rennweg.htl.netcrawler.network.graph.NetworkDevice;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;
import at.rennweg.htl.netcrawler.network.graph.SerialCable;


public class TestJNetworkGraph {
	
	public static void main(String[] args) throws Throwable {
		CiscoRouter routerA = new CiscoRouter("Router A", null);
		CiscoRouter routerB = new CiscoRouter("Router B", null);
		CiscoRouter routerC = new CiscoRouter("Router C", null);
		
		NetworkGraph<NetworkDevice, NetworkCable<NetworkDevice>> networkGraph = new NetworkGraph<NetworkDevice, NetworkCable<NetworkDevice>>();
		networkGraph.addVertex(routerA);
		networkGraph.addVertex(routerB);
		networkGraph.addVertex(routerC);
		
		EthernetCable<NetworkDevice> ethernetCable = new EthernetCable<NetworkDevice>(routerA, routerB, routerC);
		ethernetCable.setCrossover(true);
		networkGraph.addEdge(ethernetCable);
		networkGraph.addEdge(new SerialCable<NetworkDevice>(routerA, routerB));
		
		
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
	}
	
}