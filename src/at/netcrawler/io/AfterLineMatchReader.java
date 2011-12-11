package at.netcrawler.io;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

import at.andiwand.library.io.StreamUtil;


public class AfterLineMatchReader extends FilterReader {
	
	private final Pattern pattern;
	private boolean match;
	private StringBuilder line = new StringBuilder();
	
	public AfterLineMatchReader(Reader in, Pattern pattern) {
		super(in);
		
		this.pattern = pattern;
	}
	
	@Override
	public int read() throws IOException {
		if (match) return in.read();
		
		while (true) {
			int read = in.read();
			if (read == -1) return -1;
			
			if ((read == '\n') || (read == '\r')) line = new StringBuilder();
			else line.append((char) read);
			
			if (pattern.matcher(line).matches()) {
				match = true;
				break;
			}
		}
		
		return in.read();
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return StreamUtil.readCharwise(this, cbuf, off, len);
	}
	
}