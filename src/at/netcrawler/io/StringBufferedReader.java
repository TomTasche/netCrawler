package at.netcrawler.io;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

import at.andiwand.library.util.ReaderUtil;


public class StringBufferedReader extends FilterReader {
	
	private StringBuilder buffer;
	
	public StringBufferedReader(Reader in) {
		super(in);
		
		buffer = new StringBuilder();
	}
	
	public StringBuilder getBuffer() {
		return buffer;
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
		return ReaderUtil.read(
				this, cbuf, off, len);
	}
	
	public void clearBuffer() {
		buffer = new StringBuilder();
	}
	
}