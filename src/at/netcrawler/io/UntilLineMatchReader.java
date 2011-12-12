package at.netcrawler.io;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

import at.andiwand.library.io.StreamUtil;


public class UntilLineMatchReader extends FilterReader {
	
	private final Pattern pattern;
	private boolean match;
	private StringBuilder line = new StringBuilder();
	
	public UntilLineMatchReader(Reader in, Pattern pattern) {
		super(in);
		
		this.pattern = pattern;
	}
	
	protected void match() {}
	
	@Override
	public int read() throws IOException {
		if (match) return -1;
		
		int read = in.read();
		if (read == -1) return -1;
		
		if ((read == '\n') || (read == '\r')) line = new StringBuilder();
		else line.append((char) read);
		
		if (pattern.matcher(line).matches()) {
			match = true;
			match();
		}
		
		return read;
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return StreamUtil.readCharwise(this, cbuf, off, len);
	}
	
}