package test;

import graphics.graph.JGraph;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import math.graph.DefaultGraph;


public class TestJGraph {
	
	public static void main(String[] args) throws Throwable {
		int n = 100;
		
		DefaultGraph<Integer> graph = new DefaultGraph<Integer>();
		
		for (int i = 0; i < n; i++) {
			graph.addVertex(i);
		}
		
		for (int i = n - 1; i >= 0; i--) {
			for (int j = i + 1; j < n; j++) {
				graph.addEdge(i, j);
			}
		}
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		
		JGraph jGraph = new JGraph(graph);
		jGraph.setAntialiasing(true);
		JScrollPane scrollPane = new JScrollPane(jGraph);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		frame.add(scrollPane);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setVisible(true);
	}
	
}