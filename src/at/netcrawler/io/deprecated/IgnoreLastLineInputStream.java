package at.netcrawler.io.deprecated;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import at.andiwand.library.io.StreamUtil;


public class IgnoreLastLineInputStream extends FilterInputStream {
	
	private boolean lastLine;
	private final LinkedList<Character> buffer = new LinkedList<Character>();
	
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
				buffer.add((char) read);
				
				if ((read == '\n') || (read == '\r')) {
					return buffer.poll();
				}
			}
		}
		
		lastLine = true;
		buffer.clear();
		return -1;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return StreamUtil.read(this, b, off, len);
	}
	
}