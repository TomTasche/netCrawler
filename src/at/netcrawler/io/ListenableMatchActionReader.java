package at.netcrawler.io;

import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ListenableMatchActionReader extends MatchActionReader {
	
	public ListenableMatchActionReader(Reader in, Pattern pattern) {
		super(in, pattern);
	}
	
	@Override
	protected void match(Matcher matcher) {
		
	}
	
}