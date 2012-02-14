package at.netcrawler.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import at.andiwand.library.io.FluidInputStreamReader;
import at.andiwand.library.io.StreamUtil;
import at.netcrawler.io.FilterLineMatchActionReader;


public class FilterLineMatchActionReaderTest {
	
	public static void main(String[] args) throws IOException {
		byte[] buffer = "A summary of U.S. laws governing Cisco cryptographic products may be found at:\nhttp://www.cisco.com/wwl/export/crypto/tool/stqrg.html\n --More-- "
				.getBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
		FluidInputStreamReader inputStreamReader = new FluidInputStreamReader(
				inputStream);
		FilterLineMatchActionReader reader = new FilterLineMatchActionReader(
				inputStreamReader, Pattern.compile("(.+).*?(.+)more\\2.*?\\1",
						Pattern.CASE_INSENSITIVE)) {
			protected void match() {
				System.out.println("match!");
			}
		};
		
		System.out.println(StreamUtil.read(reader));
	}
	
}