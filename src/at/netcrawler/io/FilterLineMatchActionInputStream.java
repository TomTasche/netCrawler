package at.netcrawler.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.regex.Pattern;

import at.andiwand.library.io.BytewiseFilterInputStream;


public abstract class FilterLineMatchActionInputStream extends
		BytewiseFilterInputStream {
	
	private final PushbackInputStream in;
	private final Pattern pattern;
	
	private StringBuilder buffer;
	private int index;
	private boolean nextClose;
	
	public FilterLineMatchActionInputStream(InputStream in, Pattern pattern) {
		super(new PushbackInputStream(in));
		
		this.in = (PushbackInputStream) super.in;
		this.pattern = pattern;
	}
	
	protected abstract void match();
	
	@Override
	public int read() throws IOException {
		if (buffer == null) {
			if (nextClose) return -1;
			
			buffer = new StringBuilder();
			
			while (true) {
				int read = in.read();
				
				if (read == -1) nextClose = true;
				else buffer.append((char) read);
				
				if ((read == '\n') || (read == '\r') || (read == -1)) {
					if (read == '\r') {
						read = in.read();
						if (read == '\n') buffer.append((char) read);
						else in.unread(read);
					}
					
					if (buffer.length() <= 0) {
						buffer = null;
						return -1;
					}
					
					index = 0;
					break;
				}
				
				if (pattern.matcher(
						buffer).matches()) {
					match();
					buffer = new StringBuilder();
				}
			}
		}
		
		int read = buffer.charAt(index++);
		if (index >= buffer.length()) buffer = null;
		return read;
	}
	
}