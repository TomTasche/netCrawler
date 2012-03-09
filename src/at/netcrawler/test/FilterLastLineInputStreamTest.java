package at.netcrawler.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import at.andiwand.library.io.StreamUtil;
import at.netcrawler.io.FilterLastLineInputStream;


public class FilterLastLineInputStreamTest {
	
	public static void main(String[] args) throws IOException {
		byte[] buffer = "hallo\nwelt!\nkill me!!!!!".getBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
		FilterLastLineInputStream reader = new FilterLastLineInputStream(
				inputStream);
		
		System.out.println(StreamUtil.readAsString(reader));
	}
	
}