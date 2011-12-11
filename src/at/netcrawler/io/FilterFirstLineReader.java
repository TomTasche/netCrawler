package at.netcrawler.io;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

import at.andiwand.library.io.StreamUtil;


public class FilterFirstLineReader extends FilterReader {
	
	private boolean filtered;
	
	public FilterFirstLineReader(Reader in) {
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
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return StreamUtil.readCharwise(this, cbuf, off, len);
	}
	
}