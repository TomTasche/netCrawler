package at.netcrawler.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {
	
	public static void main(String[] args) {
		String s = "advertisement version: 2\n" + "Duplex: full\n"
				+ "---------------------------\n" + "\n"
				+ "Device ID: Router\n" + "Entry address(es): ";
		
		Pattern pattern = Pattern.compile("^-{2,}$", Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(s);
		
		if (!matcher.find()) return;
		
		System.out.println(matcher.group());
		
		System.out.println(s.split("^-{2,}$").length);
	}
	
}