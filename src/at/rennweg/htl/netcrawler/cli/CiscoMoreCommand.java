package at.rennweg.htl.netcrawler.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CiscoMoreCommand extends CiscoCommand {
	
	public static final Pattern MORE_PATTERN = Pattern.compile(" --More--");
	public static final byte[] NEXT_PAGE = " ".getBytes();
	
	
	public CiscoMoreCommand(InputStream inputStream, OutputStream outputStream) {
		super(inputStream, outputStream);
	}
	public CiscoMoreCommand(CiscoCommand command) {
		this(command.getInputStream(), command.getOutputStream());
	}
	
	
	@Override
	public String readWholeOutput() throws IOException {
		StringBuilder result = new StringBuilder();
		
		InputStream inputStream = getInputStream();
		OutputStream outputStream = getOutputStream();
		
		int read;
		StringBuilder line = new StringBuilder();
		
		while ((read = inputStream.read()) != -1) {
			Matcher moreMatcher = MORE_PATTERN.matcher(line);
			
			if (moreMatcher.matches()) {
				outputStream.write(NEXT_PAGE);
				outputStream.flush();
			}
			
			if ((read == '\r') || (read == '\n')) {
				line = new StringBuilder();
			} else {
				line.append((char) read);
			}
			
			result.append((char) read);
		}
		
		return result.toString();
	}
	
}