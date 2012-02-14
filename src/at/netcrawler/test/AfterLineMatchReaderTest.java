package at.netcrawler.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import at.andiwand.library.io.FluidInputStreamReader;
import at.andiwand.library.io.StreamUtil;
import at.netcrawler.io.AfterLineMatchReader;


public class AfterLineMatchReaderTest {
	
	public static void main(String[] args) throws IOException {
		byte[] buffer = "hallo\nwelt!\n:D".getBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
		FluidInputStreamReader inputStreamReader = new FluidInputStreamReader(
				inputStream);
		AfterLineMatchReader reader = new AfterLineMatchReader(
				inputStreamReader, Pattern.compile("welt!", Pattern.LITERAL));
		
		System.out.println(StreamUtil.read(reader));
	}
	
}