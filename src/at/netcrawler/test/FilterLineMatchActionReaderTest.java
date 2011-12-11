package at.netcrawler.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import at.andiwand.library.io.FluidInputStreamReader;
import at.andiwand.library.io.StreamUtil;
import at.netcrawler.io.FilterLineMatchActionReader;


public class FilterLineMatchActionReaderTest {
	
	public static void main(String[] args) throws IOException {
		byte[] buffer = "hallo\nkill me!!!!!\nwelt!".getBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
		FluidInputStreamReader inputStreamReader = new FluidInputStreamReader(
				inputStream);
		FilterLineMatchActionReader reader = new FilterLineMatchActionReader(
				inputStreamReader, Pattern.compile("kill me.*")) {
			protected void match() {
				System.out.println("match!");
			}
		};
		
		System.out.println(StreamUtil.read(reader));
	}
	
}