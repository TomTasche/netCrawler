package at.netcrawler.io;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;


public class StringBufferedReader extends FilterReader {
	
	private StringBuilder buffer;
	
	public StringBufferedReader(Reader in) {
		super(in);
		
		buffer = new StringBuilder();
	}
	
	public final String getBuffer() {
		return buffer.toString();
	}
	
	@Override
	public int read() throws IOException {
		int read = in.read();
		if (read == -1) return -1;
		buffer.append((char) read);
		return read;
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		int read = in.read(cbuf, off, len);
		if (read == -1) return -1;
		buffer.append(cbuf, off, len);
		return read;
	}
	
	public final void clearBuffer() {
		buffer = new StringBuilder();
	}
	
}