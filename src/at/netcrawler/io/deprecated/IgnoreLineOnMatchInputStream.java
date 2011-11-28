package at.netcrawler.io.deprecated;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.andiwand.library.util.StreamUtil;


public class IgnoreLineOnMatchInputStream extends FilterInputStream {
	
	private boolean closed;
	private LinkedList<Byte> buffer = new LinkedList<Byte>();
	
	private StringBuilder line = new StringBuilder();
	private Pattern pattern;
	
	public IgnoreLineOnMatchInputStream(InputStream in, Pattern pattern) {
		super(in);
		
		this.pattern = pattern;
	}
	
	@Override
	public int read() throws IOException {
		if (closed) return -1;
		
		if (!buffer.isEmpty()) {
			return buffer.poll();
		} else {
			int read;
			while ((read = in.read()) != -1) {
				buffer.add((byte) read);
				
				if ((read == '\n') || (read == '\r')) {
					Matcher matcher = pattern.matcher(line);
					if (matcher.matches()) {
						buffer.clear();
						
						if (read == '\r') {
							read = in.read();
							if (read != '\n') buffer.add((byte) read);
						}
					}
					
					line = new StringBuilder();
					return read();
				}
				
				line.append((char) read);
			}
		}
		
		closed = true;
		buffer.clear();
		return -1;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return StreamUtil.read(
				this, b, off, len);
	}
	
}