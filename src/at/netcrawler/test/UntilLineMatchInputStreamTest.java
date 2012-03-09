package at.netcrawler.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import at.andiwand.library.io.StreamUtil;
import at.netcrawler.io.UntilLineMatchInputStream;


public class UntilLineMatchInputStreamTest {
	
	public static void main(String[] args) throws IOException {
		byte[] buffer = "hallo\nwelt!\n:D".getBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
		UntilLineMatchInputStream reader = new UntilLineMatchInputStream(
				inputStream, Pattern.compile(
						"welt!", Pattern.LITERAL));
		
		System.out.println(StreamUtil.readAsString(reader));
	}
	
}