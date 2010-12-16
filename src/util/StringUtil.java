package util;

import java.util.ArrayList;
import java.util.List;


public final class StringUtil {
	
	public static final String NEW_LINE = System.getProperty("line.separator");
	
	
	public static String multiply(String string, int times) {
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < times; i++) builder.append(string);
		
		return builder.toString();
	}
	
	public static int width(String string) {
		int result = 0;
		
		String[] lines = string.split(NEW_LINE);
		
		for (int i = 0; i < lines.length; i++) {
			int length = lines[i].length();
			
			if (result < length) result = length;
		}
		
		return result;
	}
	public static int height(String string) {
		int result = 1;
		
		for (int i = string.indexOf(NEW_LINE); i != -1; i = string.indexOf(NEW_LINE, i + 1))
			result++;
		
		return result;
	}
	
	public static String[] lines(String string) {
		int index = string.indexOf(NEW_LINE);
		
		if (index == -1) return new String[] {string};
		
		List<String> lines = new ArrayList<String>();
		lines.add(string.substring(0, index));
		
		int offset = index + NEW_LINE.length();
		
		while (true) {
			index = string.indexOf(NEW_LINE, offset);
			
			if (index == -1) {
				lines.add(string.substring(offset));
				break;
			}
			
			lines.add(string.substring(offset, index));
			
			offset = index + NEW_LINE.length();
		}
		
		return lines.toArray(new String[lines.size()]);
	}
	
	public static String widthCenter(String string, int width) {
		String[] lines = lines(string);
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < lines.length; i++) {
			int spaceCount = width - lines[i].length();
			int spaceLeft = spaceCount / 2;
			int spaceRight = spaceCount - spaceLeft;
			
			lines[i] = lines[i] + multiply(" ", spaceRight);
			lines[i] = multiply(" ", spaceLeft) + lines[i];
			
			builder.append(lines[i]);
			if (i < (lines.length - 1)) builder.append(NEW_LINE);
		}
		
		return builder.toString();
	}
	public static String heightCenter(String string, int height) {
		int stringHeight = height(string);
		
		if (stringHeight >= height) return string;
		
		int spaceCount = height - stringHeight;
		int spaceTop = spaceCount / 2;
		int spaceBottom = spaceCount - spaceTop;
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(multiply(NEW_LINE, spaceTop));
		builder.append(string);
		builder.append(multiply(NEW_LINE, spaceBottom));
		
		return builder.toString();
	}
	public static String center(String string, int width, int height) {
		string = heightCenter(string, height);
		string = widthCenter(string, width);
		
		return string;
	}
	
	
	private StringUtil() {}
	
}