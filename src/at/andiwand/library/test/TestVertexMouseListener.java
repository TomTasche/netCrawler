package at.andiwand.library.test;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import at.andiwand.library.graphics.graph.DrawableVertex;
import at.andiwand.library.graphics.graph.JGraph;
import at.andiwand.library.graphics.graph.VertexMouseAdapter;
import at.andiwand.library.math.graph.DefaultGraph;


public class TestVertexMouseListener {
	
	public static void main(String[] args) throws Throwable {
		int n = 10;
		
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
		jGraph.setMagneticLines(true);
		
		jGraph.addVertexMouseListener(new VertexMouseAdapter() {
			private static final long serialVersionUID = 6800701239267574683L;
			
			public void mouseClicked(MouseEvent e) {
				DrawableVertex vertex = (DrawableVertex) e.getSource();
				JGraph jGraph = vertex.getGraph();
				
				JPopupMenu menu = new JPopupMenu();
				menu.add(new JMenuItem("hallo :)"));
				menu.show(jGraph, e.getX(), e.getY());
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(jGraph);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		frame.add(scrollPane);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setVisible(true);
	}
	
}