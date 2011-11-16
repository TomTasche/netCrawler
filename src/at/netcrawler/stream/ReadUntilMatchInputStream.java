package at.netcrawler.stream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReadUntilMatchInputStream extends FilterInputStream {
	
	private StringBuilder line = new StringBuilder();
	private Pattern pattern;
	private Matcher matcher;
	
	private boolean closed;
	
	
	public ReadUntilMatchInputStream(InputStream in, Pattern pattern) {
		super(in);
		
		this.pattern = pattern;
	}
	
	
	public Matcher getFinalMatcher() {
		if (!closed) return null;
		
		return matcher;
	}
	
	
	@Override
	public int read() throws IOException {
		if (closed) return -1;
		
		int read = in.read();
		if (read == -1) {
			closed = true;
			return -1;
		}
		
		line.append((char) read);
		
		matcher = pattern.matcher(line);
		if (matcher.matches()) closed = true;
		
		if ((read == '\n') || (read == '\r')) {
			line = new StringBuilder();
		}
		
		return read;
	}
	
}