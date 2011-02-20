package at.andiwand.library.util.stream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;


public class IgnoreLastLineInputStream extends FilterInputStream {
	
	private boolean lastLine;
	private LinkedList<Byte> buffer = new LinkedList<Byte>();
	
	
	public IgnoreLastLineInputStream(InputStream in) {
		super(in);
	}
	
	
	@Override
	public int read() throws IOException {
		if (lastLine) return -1;
		
		if (!buffer.isEmpty()) {
			return buffer.poll();
		} else {
			int read;
			while ((read = in.read()) != -1) {
				buffer.add((byte) read);
				
				if ((read == '\n') || (read == '\r')) {
					return buffer.poll();
				}
			}
		}
		
		lastLine = true;
		buffer.clear();
		return -1;
	}
	
}