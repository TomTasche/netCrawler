package test;

import java.util.ArrayList;
import java.util.List;

import math.graph.walk.DefaultWalk;



public class TestDefaultWalk {
	
	public static void main(String[] args) {
		List<String> vertexSequnence = new ArrayList<String>();
		vertexSequnence.add("A");
		vertexSequnence.add("B");
		vertexSequnence.add("C");
		vertexSequnence.add("A");
		
		DefaultWalk<String> walk = new DefaultWalk<String>(vertexSequnence);
		System.out.println(walk.getEdgeSequence());
		System.out.println(walk.isCycle());
	}
	
}