package at.rennweg.htl.netcrawler.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import at.andiwand.library.util.cli.CommandLine;
import at.andiwand.library.util.cli.CommandLineExecutor;
import at.andiwand.library.util.stream.IgnoreFirstLineInputStream;
import at.andiwand.library.util.stream.ReadAfterMatchInputStream;
import at.andiwand.library.util.stream.ReadUntilMatchInputStream;



public class CiscoCommandLineExecutor extends CommandLineExecutor {
	
	public static final Pattern PROMT_PATTERN = Pattern.compile("(.*?)(/.*?)?(\\((.*?)\\))?(>|#)");
	
	
	
	private InputStream inputStream;
	private OutputStream outputStream;
	
	
	
	public CiscoCommandLineExecutor(CommandLine commandLine) throws IOException {
		this(commandLine, null);
	}
	public CiscoCommandLineExecutor(CommandLine commandLine, CiscoUser user) throws IOException {
		super(commandLine);
		
		inputStream = commandLine.getInputStream();
		outputStream = commandLine.getOutputStream();
		
		if (user != null) {
			outputStream.write(user.getUsername().getBytes());
			outputStream.flush();
			outputStream.write("\r\n".getBytes());
			outputStream.flush();
			
			for (int i = 0; i < user.getPassword().length(); i++) {
				outputStream.write(user.getPassword().charAt(i));
				outputStream.flush();
			}
			
			outputStream.write("\r\n".getBytes());
			outputStream.flush();
		}
		
		InputStream killerStream = new ReadUntilMatchInputStream(inputStream, PROMT_PATTERN);
		
		try {
			while (killerStream.read() != -1);
		} catch (IOException e) {}
	}
	
	
	
	@Override
	public CiscoCommand execute(String command) throws IOException {
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
		
		outputStream.write("\r\n".getBytes());
		outputStream.flush();
		
		return new CiscoCommand(stream3, commandLine.getOutputStream());
	}
	
	public CiscoMoreCommand executeMore(String command) throws IOException {
		return new CiscoMoreCommand(execute(command));
	}
	
}