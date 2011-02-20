package at.andiwand.library.util.cli;

import java.io.IOException;
import java.io.InputStream;


public abstract class Command extends Process {
	
	public String readWholeOutput() throws IOException {
		StringBuilder result = new StringBuilder();
		
		InputStream inputStream = getInputStream();
		int read;
		
		while ((read = inputStream.read()) != -1) {
			result.append((char) read);
		}
		
		return result.toString();
	}
	
}