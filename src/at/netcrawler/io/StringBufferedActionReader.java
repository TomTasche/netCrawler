package at.netcrawler.io;

import java.io.IOException;
import java.io.Reader;


public abstract class StringBufferedActionReader extends StringBufferedReader {
	
	public StringBufferedActionReader(Reader in) {
		super(in);
	}
	
	@Override
	public int read() throws IOException {
		int result = super.read();
		if (result == -1) return -1;
		
		newChar((char) result);
		
		return result;
	}
	
	protected abstract void newChar(char c);
	
}