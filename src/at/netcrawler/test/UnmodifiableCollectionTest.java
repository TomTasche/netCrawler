package at.netcrawler.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class UnmodifiableCollectionTest {
	
	public static void main(String[] args) {
		Set<Object> set = new HashSet<Object>();
		set.add("hallo");
		
		Set<Object> unmodifiableSet = Collections.unmodifiableSet(set);
		
		System.out.println(set.getClass().getName());
		System.out.println(unmodifiableSet.getClass().getName());
		System.out.println();
		System.out.println();
		
		System.out.println(set);
		System.out.println(unmodifiableSet);
		System.out.println();
		
		set.add("welt");
		
		System.out.println(set);
		System.out.println(unmodifiableSet);
		System.out.println();
		
		unmodifiableSet.add("!");
		
		System.out.println(set);
		System.out.println(unmodifiableSet);
	}
	
}