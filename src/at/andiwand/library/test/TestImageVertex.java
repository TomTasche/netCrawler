package at.andiwand.library.test;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import at.andiwand.library.graphics.graph.CircleGraphLayout;
import at.andiwand.library.graphics.graph.ImageVertexFactory;
import at.andiwand.library.graphics.graph.JGraph;
import at.andiwand.library.math.graph.DefaultGraph;


public class TestImageVertex {
	
	public static void main(String[] args) throws Throwable {
		final int n = 108;
		
		final DefaultGraph<Integer> graph = new DefaultGraph<Integer>();
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		
		ImageIcon image = new ImageIcon(TestImageVertex.class.getResource("router.png"));
		ImageVertexFactory vertexFactory = new ImageVertexFactory(image);
		
		JGraph jGraph = new JGraph();
		jGraph.setGraphLayout(new CircleGraphLayout(jGraph, 300));
		jGraph.setMagneticLines(true);
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