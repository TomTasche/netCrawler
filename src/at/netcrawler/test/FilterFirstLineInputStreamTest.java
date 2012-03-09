package at.netcrawler.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import at.andiwand.library.io.StreamUtil;
import at.netcrawler.io.FilterFirstLineInputStream;


public class FilterFirstLineInputStreamTest {
	
	public static void main(String[] args) throws IOException {
		byte[] buffer = "\nhallo welt!".getBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
		FilterFirstLineInputStream reader = new FilterFirstLineInputStream(
				inputStream);
		
		System.out.println(StreamUtil.readAsString(reader));
	}
	
}