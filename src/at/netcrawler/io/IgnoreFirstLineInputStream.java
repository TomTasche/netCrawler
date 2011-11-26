package at.netcrawler.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import at.andiwand.library.util.StreamUtil;


public class IgnoreFirstLineInputStream extends FilterInputStream {
	
	private boolean firstLine;
	
	
	public IgnoreFirstLineInputStream(InputStream in) {
		super(in);
	}
	
	
	@Override
	public int read() throws IOException {
		if (firstLine) return in.read();
		
		int read;
		while ((read = in.read()) != -1) {
			if ((read == '\n') || (read == '\r')) {
				if (read == '\r') {
					read = in.read();
					if (read == '\n') {
						read = in.read();
					}
				} else {
					read = in.read();
				}
				
				firstLine = true;
				break;
			}
		}
		
		return read;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return StreamUtil.read(this, b, off, len);
	}
	
}