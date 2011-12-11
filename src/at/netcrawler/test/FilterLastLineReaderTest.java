package at.netcrawler.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import at.andiwand.library.io.FluidInputStreamReader;
import at.andiwand.library.io.StreamUtil;
import at.netcrawler.io.FilterLastLineReader;


public class FilterLastLineReaderTest {
	
	public static void main(String[] args) throws IOException {
		byte[] buffer = "hallo\nwelt!\nkill me!!!!!".getBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
		FluidInputStreamReader inputStreamReader = new FluidInputStreamReader(
				inputStream);
		FilterLastLineReader reader = new FilterLastLineReader(
				inputStreamReader);
		
		System.out.println(StreamUtil.read(reader));
	}
	
}