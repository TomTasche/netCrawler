package at.rennweg.htl.netcrawler.test;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import at.rennweg.htl.netcrawler.graphics.graph.JNetworkGraph;
import at.rennweg.htl.netcrawler.network.graph.CiscoRouter;
import at.rennweg.htl.netcrawler.network.graph.EthernetCable;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;
import at.rennweg.htl.netcrawler.network.graph.NetworkInterface;
import at.rennweg.htl.netcrawler.network.graph.SerialCable;


public class TestJNetworkGraph {
	
	public static void main(String[] args) throws Throwable {
		CiscoRouter routerA = new CiscoRouter("Router A", null);
		routerA.addInterface(new NetworkInterface("fa0/0"));
		routerA.addInterface(new NetworkInterface("s0/0"));
		CiscoRouter routerB = new CiscoRouter("Router B", null);
		routerB.addInterface(new NetworkInterface("fa0/0"));
		routerB.addInterface(new NetworkInterface("s0/0"));
		CiscoRouter routerC = new CiscoRouter("Router C", null);
		routerC.addInterface(new NetworkInterface("fa0/0"));
		
		NetworkGraph networkGraph = new NetworkGraph();
		networkGraph.addVertex(routerA);
		networkGraph.addVertex(routerB);
		networkGraph.addVertex(routerC);
		
		EthernetCable ethernetCable = new EthernetCable(
				routerA.getInterface("fa0/0"),
				routerB.getInterface("fa0/0"),
				routerC.getInterface("fa0/0")
		);
		ethernetCable.setCrossover(true);
		networkGraph.addEdge(ethernetCable);
		networkGraph.addEdge(new SerialCable(
				routerA.getInterface("s0/0"),
				routerB.getInterface("s0/0")
		));
		
		
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