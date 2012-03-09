package at.netcrawler.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.io.BytewiseFilterInputStream;
import at.andiwand.library.io.StreamUtil;


public class CharPrefixLineFilterInputStream extends BytewiseFilterInputStream {
	
	private final PushbackInputStream in;
	private final Set<Character> prefixes;
	
	private boolean newLine = true;
	
	public CharPrefixLineFilterInputStream(InputStream in) {
		this(in, new ArrayList<Character>(0));
	}
	
	public CharPrefixLineFilterInputStream(InputStream in,
			Collection<Character> prefixes) {
		super(new PushbackInputStream(in));
		
		this.in = (PushbackInputStream) super.in;
		this.prefixes = new HashSet<Character>(prefixes);
	}
	
	public CharPrefixLineFilterInputStream(InputStream in, char... prefixes) {
		this(in);
		
		for (char prefix : prefixes) {
			addPrefix(prefix);
		}
	}
	
	public boolean addPrefix(char prefix) {
		if ((prefix == '\n') || (prefix == '\r')) return false;
		return prefixes.add(prefix);
	}
	
	public boolean removePrefix(char prefix) {
		return prefixes.remove(prefix);
	}
	
	@Override
	public int read() throws IOException {
		int read;
		
		while (true) {
			read = in.read();
			if (read == -1) return -1;
			
			if (newLine && prefixes.contains((char) read)) {
				StreamUtil.flushLine(in);
				continue;
			}
			
			newLine = false;
			break;
		}
		
		if ((read == '\n') || (read == '\r')) newLine = true;
		return read;
	}
	
}