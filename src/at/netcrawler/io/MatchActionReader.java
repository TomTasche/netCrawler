package at.netcrawler.io;

import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class MatchActionReader extends StringBufferedActionReader {
	
	private final Pattern pattern;
	
	public MatchActionReader(Reader in, Pattern pattern) {
		super(in);
		
		this.pattern = pattern;
	}
	
	@Override
	protected void newChar(char c) {
		Matcher matcher = pattern.matcher(getBuffer());
		if (!matcher.matches()) return;
		
		match(matcher);
		clearBuffer();
	}
	
	protected abstract void match(Matcher matcher);
	
}