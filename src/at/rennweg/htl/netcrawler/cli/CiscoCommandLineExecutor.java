package at.rennweg.htl.netcrawler.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.andiwand.library.util.StringUtil;
import at.andiwand.library.util.cli.CommandLine;
import at.andiwand.library.util.cli.CommandLineExecutor;
import at.andiwand.library.util.stream.IgnoreFirstLineInputStream;
import at.andiwand.library.util.stream.ReadAfterMatchInputStream;
import at.andiwand.library.util.stream.ReadUntilMatchInputStream;


public class CiscoCommandLineExecutor extends CommandLineExecutor {
	
	public static final Pattern PROMT_PATTERN = Pattern.compile("(.*?)(/.*?)?(\\((.*?)\\))?(>|#)");
	public static final Pattern MORE_PATTERN = Pattern.compile(" *-+ ?more ?-+ *", Pattern.CASE_INSENSITIVE);
	
	
	private InputStream inputStream;
	
	public CiscoCommandLineExecutor(CommandLine commandLine) throws IOException {
		super(commandLine);
		
		inputStream = commandLine.getInputStream();
		
		InputStream killerStream = new ReadUntilMatchInputStream(inputStream, PROMT_PATTERN);
		
		try {
			while (killerStream.read() != -1);
		} catch (IOException e) {}
	}
	
	@Override
	public String execute(String command) throws IOException {
		Pattern commandPattern = Pattern.compile(".*" + command);
		
		InputStream inputStream = commandLine.getInputStream();
		InputStream stream1 = new ReadAfterMatchInputStream(inputStream, commandPattern);
		InputStream stream2 = new IgnoreFirstLineInputStream(stream1);
		InputStream stream3 = new ReadUntilMatchInputStream(stream2, PROMT_PATTERN);
		//InputStream stream4 = new IgnoreLastLineInputStream(stream3);
		
		OutputStream outputStream = commandLine.getOutputStream();
		for (int i = 0; i < command.length(); i++) {
			outputStream.write(command.charAt(i));
			outputStream.flush();
		}
		outputStream.write(StringUtil.NEW_LINE.getBytes());
		outputStream.flush();
		
		StringBuilder builder = new StringBuilder();
		
		int read;
		while ((read = stream3.read()) != -1) {
			int lastLine = builder.lastIndexOf(StringUtil.NEW_LINE);
			if (lastLine != -1) {
				Matcher matcher = MORE_PATTERN.matcher(builder.substring(lastLine + 1));
				
				if (matcher.matches()) {
					outputStream.write(" ".getBytes());
					outputStream.flush();
				}
			}
			
			builder.append((char) read);
		}
		
		return builder.toString();
	}
	
}