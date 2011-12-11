package at.netcrawler.io;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

import at.andiwand.library.io.StreamUtil;


public class FilterLastLineReader extends FilterReader {
	
	private StringBuilder buffer;
	private Character first;
	private int index;
	private boolean closed;
	
	public FilterLastLineReader(Reader in) {
		super(in);
	}
	
	@Override
	public int read() throws IOException {
		if (closed) return -1;
		
		if (buffer == null) {
			buffer = new StringBuilder();
			
			while (true) {
				int read;
				
				if (first == null) {
					read = in.read();
				} else {
					read = first;
					first = null;
				}
				
				if (read == -1) {
					closed = true;
					return -1;
				}
				
				buffer.append((char) read);
				
				if ((read == '\n') || (read == '\r')) {
					if (read == '\r') {
						read = in.read();
						if (read == '\n') buffer.append((char) read);
						else first = (char) read;
					}
					
					index = 0;
					break;
				}
			}
		}
		
		int read = buffer.charAt(index++);
		if (index >= buffer.length()) buffer = null;
		return read;
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return StreamUtil.readCharwise(this, cbuf, off, len);
	}
	
}