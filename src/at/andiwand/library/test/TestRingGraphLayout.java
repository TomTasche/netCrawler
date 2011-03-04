package at.andiwand.library.test;


import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import at.andiwand.library.graphics.graph.ImageVertexFactory;
import at.andiwand.library.graphics.graph.JGraph;
import at.andiwand.library.graphics.graph.RingGraphLayout;
import at.andiwand.library.math.graph.DefaultGraph;



public class TestRingGraphLayout {
	
	public static void main(String[] args) throws Throwable {
		final int n = 20;
		
		final DefaultGraph<Integer> graph = new DefaultGraph<Integer>();
		
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		
		Image image = Toolkit.getDefaultToolkit().createImage(TestImageVertex.class.getResource("router.png"));
		while (image.getWidth(null) < 0) Thread.sleep(100);
		ImageVertexFactory vertexFactory = new ImageVertexFactory(image);
		
		JGraph jGraph = new JGraph();
		jGraph.addVertexFactory(Object.class, vertexFactory);
		jGraph.setGraphLayout(new RingGraphLayout(jGraph, 0));
		jGraph.setGraph(graph);
		jGraph.setMagneticLines(true);
		jGraph.setAntialiasing(true);
		JScrollPane scrollPane = new JScrollPane(jGraph);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		frame.add(scrollPane);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 800);
		frame.setVisible(true);
		
		
		new Thread() {
			public void run() {
				for (int i = 0; i < n; i++) {
					graph.addVertex(i);
					
					for (int j = 0; j < i; j++) {
						graph.addEdge(i, j);
					}
				}
			}
		}.start();
	}
	
}