package at.netcrawler.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.andiwand.library.util.StreamUtil;


public class ReadAfterMatchInputStream extends FilterInputStream {
	
	private StringBuilder line = new StringBuilder();
	private Pattern pattern;
	private Matcher matcher;
	private boolean matched;
	
	
	public ReadAfterMatchInputStream(InputStream in, Pattern pattern) {
		super(in);
		
		this.pattern = pattern;
	}
	
	
	public Matcher getFinalMatcher() {
		if (!matched) return null;
		
		return matcher;
	}
	
	
	@Override
	public int read() throws IOException {
		if (matched) return in.read();
		
		int read;
		while ((read = in.read()) != -1) {
			line.append((char) read);
			
			matcher = pattern.matcher(line);
			if (matcher.matches()) {
				matched = true;
				break;
			}
			
			if ((read == '\n') || (read == '\r')) {
				line = new StringBuilder();
			}
		}
		
		return in.read();
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return StreamUtil.read(this, b, off, len);
	}
	
}