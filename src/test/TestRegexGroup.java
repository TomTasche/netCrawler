package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestRegexGroup {
	
	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("^.* (.*?) Software \\((.*?)\\).*$");
		System.out.println(pattern);
		
		System.out.println("---string1---");
		
		String string1 = "IOS (tm) C2950 Software (C2950-I6Q4L2-M)";
		Matcher matcher1 = pattern.matcher(string1);
		System.out.println(matcher1.matches());
		System.out.println(matcher1.groupCount());
		for (int group = 0; group <= matcher1.groupCount(); group++) {
			System.out.println(matcher1.group(group));
		}
		
		System.out.println("---string2---");
		
		String string2 = "Cisco IOS Software, C2960 Software (C2960-LANBASE-M), Version 12.2(25)FX, RELEASE SOFTWARE (fc1)";
		Matcher matcher2 = pattern.matcher(string2);
		System.out.println(matcher2.matches());
		System.out.println(matcher2.groupCount());
		for (int group = 0; group <= matcher2.groupCount(); group++) {
			System.out.println(matcher2.group(group));
		}
	}
	
}