package at.netcrawler.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import at.andiwand.library.io.BytewiseFilterInputStream;


public class UntilLineMatchInputStream extends BytewiseFilterInputStream {
	
	private final Pattern pattern;
	private boolean match;
	private StringBuilder line = new StringBuilder();
	
	public UntilLineMatchInputStream(InputStream in, Pattern pattern) {
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
	
}