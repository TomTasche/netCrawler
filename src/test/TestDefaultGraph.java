package test;

import math.graph.DefaultGraph;


public class TestDefaultGraph {
	
	public static void main(String[] args) {
		DefaultGraph<String> graph = new DefaultGraph<String>();
		
		System.out.println(graph.isConnected());
		System.out.println(graph.isSimple());
		System.out.println();
		
		graph.addVertex("A");
		graph.addVertex("B");
		
		System.out.println(graph.isConnected());
		System.out.println(graph.isSimple());
		System.out.println();
		
		System.out.println(graph.addEdge("A", "B"));
		System.out.println(graph.addEdge("A", "C"));
		System.out.println();
		
		System.out.println(graph.isConnected());
		System.out.println(graph.isSimple());
		System.out.println();
		
		System.out.println(graph.addEdge("A", "B"));
		System.out.println();
		
		System.out.println(graph.isConnected());
		System.out.println(graph.isSimple());
		System.out.println();
	}
	
}