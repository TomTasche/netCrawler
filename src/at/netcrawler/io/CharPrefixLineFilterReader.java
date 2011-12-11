package at.netcrawler.io;

import java.io.FilterReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.io.StreamUtil;


public class CharPrefixLineFilterReader extends FilterReader {
	
	private final PushbackReader in;
	private final Set<Character> prefixSet = new HashSet<Character>();
	
	private boolean newLine = true;
	
	public CharPrefixLineFilterReader(Reader in) {
		super(new PushbackReader(in));
		
		this.in = (PushbackReader) super.in;
	}
	
	public CharPrefixLineFilterReader(Reader in, char... prefixes) {
		this(in);
		
		for (char prefix : prefixes)
			addPrefix(prefix);
	}
	
	public boolean addPrefix(char prefix) {
		if ((prefix == '\n') || (prefix == '\r')) return false;
		
		return prefixSet.add(prefix);
	}
	
	public boolean removePrefix(char prefix) {
		return prefixSet.remove(prefix);
	}
	
	private boolean matchPrefix(char c) {
		for (char prefix : prefixSet) {
			if (c == prefix) return true;
		}
		
		return false;
	}
	
	@Override
	public int read() throws IOException {
		int read;
		
		while (true) {
			read = in.read();
			if (read == -1) return -1;
			
			if (newLine && matchPrefix((char) read)) {
				StreamUtil.flushLine(in);
				continue;
			}
			
			newLine = false;
			break;
		}
		
		if ((read == '\n') || (read == '\r')) newLine = true;
		
		return read;
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return StreamUtil.readCharwise(this, cbuf, off, len);
	}
	
}