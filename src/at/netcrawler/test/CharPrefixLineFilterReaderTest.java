package at.netcrawler.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import at.andiwand.library.io.FluidInputStreamReader;
import at.andiwand.library.io.StreamUtil;
import at.netcrawler.io.CharPrefixLineFilterReader;


public class CharPrefixLineFilterReaderTest {
	
	public static void main(String[] args) throws IOException {
		byte[] buffer = "%asdfasdfasdf\n$asdfasdf\nhallo welt!\n%asdf"
				.getBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
		FluidInputStreamReader inputStreamReader = new FluidInputStreamReader(
				inputStream);
		CharPrefixLineFilterReader reader = new CharPrefixLineFilterReader(
				inputStreamReader, '%', '$');
		
		System.out.println(StreamUtil.read(reader));
	}
	
}