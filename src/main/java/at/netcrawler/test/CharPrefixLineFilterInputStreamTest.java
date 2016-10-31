package at.netcrawler.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import at.andiwand.library.io.StreamUtil;
import at.netcrawler.io.CharPrefixLineFilterInputStream;


public class CharPrefixLineFilterInputStreamTest {
	
	public static void main(String[] args) throws IOException {
		byte[] buffer = "%asdfasdfasdf\n$asdfasdf\nhallo welt!\n%asdf"
				.getBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
		CharPrefixLineFilterInputStream reader = new CharPrefixLineFilterInputStream(
				inputStream, '%', '$');
		
		System.out.println(StreamUtil.readAsString(reader));
	}
	
}