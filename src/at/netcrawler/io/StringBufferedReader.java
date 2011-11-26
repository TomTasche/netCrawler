package at.netcrawler.io;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;


public class StringBufferedReader extends FilterReader {
	
	private final StringBuilder builder;
	
	public StringBufferedReader(Reader in) {
		super(in);
		
		builder = new StringBuilder();
	}
	
	@Override
	public int read() throws IOException {
		int read = in.read();
		if (read == -1) return -1;
		builder.append((char) read);
		return read;
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return super.read(cbuf, off, len);
	}
	
}