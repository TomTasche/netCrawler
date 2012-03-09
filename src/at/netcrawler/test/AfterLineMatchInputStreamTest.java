package at.netcrawler.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import at.andiwand.library.io.StreamUtil;
import at.netcrawler.io.AfterLineMatchInputStream;


public class AfterLineMatchInputStreamTest {
	
	public static void main(String[] args) throws IOException {
		byte[] buffer = "hallo\nwelt!\n:D".getBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
		AfterLineMatchInputStream reader = new AfterLineMatchInputStream(
				inputStream, Pattern.compile("welt!", Pattern.LITERAL));
		
		System.out.println(StreamUtil.readAsString(reader));
	}
	
}