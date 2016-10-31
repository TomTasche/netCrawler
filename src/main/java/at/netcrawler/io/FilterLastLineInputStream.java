package at.netcrawler.io;

import java.io.IOException;
import java.io.InputStream;

import at.andiwand.library.io.BytewiseFilterInputStream;


public class FilterLastLineInputStream extends BytewiseFilterInputStream {
	
	private StringBuilder buffer;
	private StringBuilder newLineBuffer;
	private Character first;
	private int index;
	private boolean closed;
	
	public FilterLastLineInputStream(InputStream in) {
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
	
}