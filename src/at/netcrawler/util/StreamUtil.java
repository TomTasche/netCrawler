package at.netcrawler.util;

import java.io.IOException;
import java.io.InputStream;


public class StreamUtil {
	
	public static void killStream(InputStream inputStream) throws IOException {
		while (inputStream.read() != -1) ;
	}
	
	public static String readStream(InputStream inputStream) throws IOException {
		StringBuilder builder = new StringBuilder();
		
		int read;
		while ((read = inputStream.read()) != -1) {
			builder.append((char) read);
		}
		
		return builder.toString();
	}
	
	private StreamUtil() {}
	
}