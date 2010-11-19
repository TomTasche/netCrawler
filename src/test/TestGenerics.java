package test;

import math.graph.DefaultGraph;


public class TestGenerics {
	
	public static void main(String[] args) {
		DefaultGraph<String> graph = new DefaultGraph<String>();
		
		System.out.println(graph.getClass());
		System.out.println(graph.getClass().getComponentType());
	}
	
}