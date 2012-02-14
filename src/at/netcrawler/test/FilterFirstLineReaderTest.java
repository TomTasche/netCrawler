package at.netcrawler.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import at.andiwand.library.io.FluidInputStreamReader;
import at.andiwand.library.io.StreamUtil;
import at.netcrawler.io.FilterFirstLineReader;


public class FilterFirstLineReaderTest {
	
	public static void main(String[] args) throws IOException {
		byte[] buffer = "\nhallo welt!".getBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
		FluidInputStreamReader inputStreamReader = new FluidInputStreamReader(
				inputStream);
		FilterFirstLineReader reader = new FilterFirstLineReader(
				inputStreamReader);
		
		System.out.println(StreamUtil.read(reader));
	}
	
}