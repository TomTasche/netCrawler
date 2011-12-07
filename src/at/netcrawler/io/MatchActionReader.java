package at.netcrawler.io;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.andiwand.library.io.ReaderUtil;


public abstract class MatchActionReader extends StringBufferedReader {
	
	private final Pattern pattern;
	
	public MatchActionReader(Reader in, Pattern pattern) {
		super(in);
		
		this.pattern = pattern;
	}
	
	@Override
	public final int read() throws IOException {
		int read = super.read();
		if (read == -1) return -1;
		
		Matcher matcher = pattern.matcher(getBuffer());
		if (matcher.matches()) {
			match(matcher);
			clearBuffer();
		}
		
		return read;
	}
	
	@Override
	public final int read(char[] cbuf, int off, int len) throws IOException {
		return ReaderUtil.read(this, cbuf, off, len);
	}
	
	protected abstract void match(Matcher matcher);
	
}