package test;

import graphics.graph.CircleGraphLayout;
import graphics.graph.ImageVertexFactory;
import graphics.graph.JGraph;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;


import math.graph.DefaultGraph;


public class TestImageVertex {
	
	public static void main(String[] args) throws Throwable {
		final int n = 108;
		
		final DefaultGraph<Integer> graph = new DefaultGraph<Integer>();
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		
		Image image = Toolkit.getDefaultToolkit().createImage(TestImageVertex.class.getResource("router.png"));
		while (image.getWidth(null) < 0) Thread.sleep(100);
		ImageVertexFactory vertexFactory = new ImageVertexFactory(image);
		
		JGraph jGraph = new JGraph();
		jGraph.setGraphLayout(new CircleGraphLayout(jGraph, 300));
		jGraph.setMagneticRaster(true);
		jGraph.setAntialiasing(true);
		jGraph.addVertexFactory(Object.class, vertexFactory);
		jGraph.setGraph(graph);
		JScrollPane scrollPane = new JScrollPane(jGraph);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		frame.add(scrollPane);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 800);
		frame.setVisible(true);
		
		Thread thread = new Thread() {
			public void run() {
				for (int i = 0; i < n; i++) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {}
					
					graph.addVertex(i);
					
					for (int j = 0; j < i; j++) {
						graph.addEdge(i, j);
					}
				}
			}
		};
		
		thread.start();
	}
	
}