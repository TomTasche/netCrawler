package at.netcrawler.io;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ListenableMatchActionReader extends MatchActionReader {
	
	private final List<MatchActionListener> listeners = new ArrayList<MatchActionListener>();
	
	public ListenableMatchActionReader(Reader in, Pattern pattern) {
		super(in, pattern);
	}
	
	@Override
	protected void match(Matcher matcher) {
		for (MatchActionListener listener : listeners) {
			listener.match(matcher);
		}
	}
	
}