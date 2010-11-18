package at.rennweg.htl.test;

import at.rennweg.htl.math.graph.DefaultGraph;


public class TestGenerics {
	
	public static void main(String[] args) {
		DefaultGraph<String> graph = new DefaultGraph<String>();
		
		System.out.println(graph.getClass());
		System.out.println(graph.getClass().getComponentType());
	}
	
}