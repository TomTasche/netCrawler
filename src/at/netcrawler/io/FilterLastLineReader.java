package at.netcrawler.io;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

import at.andiwand.library.io.StreamUtil;


public class FilterLastLineReader extends FilterReader {
	
	private StringBuilder buffer;
	private StringBuilder newLineBuffer;
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
			if (newLineBuffer != null) buffer.append(newLineBuffer);
			newLineBuffer = new StringBuilder();
			
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
				
				if ((read == '\n') || (read == '\r')) {
					newLineBuffer.append((char) read);
					
					if (read == '\r') {
						read = in.read();
						if (read == '\n') newLineBuffer.append((char) read);
						else first = (char) read;
					}
					
					if (buffer.length() == 0) {
						buffer = null;
						return read();
					}
					
					index = 0;
					break;
				}
				
				buffer.append((char) read);
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