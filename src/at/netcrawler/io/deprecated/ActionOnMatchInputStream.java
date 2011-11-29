package at.netcrawler.io.deprecated;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.andiwand.library.util.StreamUtil;


public class ActionOnMatchInputStream extends FilterInputStream {
	
	private StringBuilder line = new StringBuilder();
	private final Pattern pattern;
	
	private final List<ActionOnMatchListener> listeners = new ArrayList<ActionOnMatchListener>();
	
	public ActionOnMatchInputStream(InputStream in, Pattern pattern) {
		super(in);
		
		this.pattern = pattern;
	}
	
	public void addListener(ActionOnMatchListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ActionOnMatchListener listener) {
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
			
			for (ActionOnMatchListener listener : listeners) {
				listener.matchOccurred(match);
			}
		}
		
		return read;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return StreamUtil.read(this, b, off, len);
	}
	
}