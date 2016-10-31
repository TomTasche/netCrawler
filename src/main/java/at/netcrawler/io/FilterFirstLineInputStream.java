package at.netcrawler.io;

import java.io.IOException;
import java.io.InputStream;

import at.andiwand.library.io.BytewiseFilterInputStream;


public class FilterFirstLineInputStream extends BytewiseFilterInputStream {
	
	private boolean filtered;
	
	public FilterFirstLineInputStream(InputStream in) {
		super(in);
	}
	
	@Override
	public int read() throws IOException {
		if (filtered) return in.read();
		
		int read;
		while (true) {
			read = in.read();
			if (read == -1) return -1;
			
			if (read == '\n') {
				filtered = true;
				read = in.read();
				break;
			} else if (read == '\r') {
				filtered = true;
				read = in.read();
				if (read == '\n') read = in.read();
				break;
			}
		}
		
		return read;
	}
	
}