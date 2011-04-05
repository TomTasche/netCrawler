package at.rennweg.htl.netcrawler.util.stream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ActionOnMatchInputStream extends FilterInputStream {
	
	private StringBuilder line = new StringBuilder();
	private final Pattern pattern;
	
	private final List<MatchActionListener> listeners =
		new ArrayList<MatchActionListener>();
	
	
	public ActionOnMatchInputStream(InputStream in, Pattern pattern) {
		super(in);
		
		this.pattern = pattern;
	}
	
	
	public void addListener(MatchActionListener listener) {
		listeners.add(listener);
	}
	public void removeListener(MatchActionListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public int read() throws IOException {
		int read = in.read();
		if (read == -1) return -1;
		
		if ((read == '\n') || (read == '\r')) line = new StringBuilder();
		else line.append((char) read);
		
		Matcher matcher = pattern.matcher(line);
		if (matcher.matches()) {
			String match = matcher.group();
			
			for (MatchActionListener listener : listeners) {
				listener.matchOccurred(match);
			}
		}
		
		return read;
	}
	
}