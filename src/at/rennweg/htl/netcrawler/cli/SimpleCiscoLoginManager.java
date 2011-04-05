package at.rennweg.htl.netcrawler.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import at.andiwand.library.util.cli.CommandLine;
import at.rennweg.htl.netcrawler.util.stream.ReadUntilMatchInputStream;


public class SimpleCiscoLoginManager {
	
	public static final Pattern USERNAME_PATTERN = Pattern.compile("username: ", Pattern.CASE_INSENSITIVE);
	public static final Pattern PASSWORD_PATTERN = Pattern.compile("password: ", Pattern.CASE_INSENSITIVE);
	
	public static final Pattern PROMT_PATTERN = SimpleCiscoCommandLineExecutor.PROMT_PATTERN;
	public static final int PROMT_TIMEOUT = 1000;
	
	
	private final CommandLine commandLine;
	
	public SimpleCiscoLoginManager(CommandLine commandLine) {
		this.commandLine = commandLine;
	}
	
	public boolean login(SimpleCiscoUser user) throws IOException {
		final InputStream inputStream = commandLine.getInputStream();
		OutputStream outputStream = commandLine.getOutputStream();
		InputStream killerStream;
		
		killerStream = new ReadUntilMatchInputStream(inputStream, USERNAME_PATTERN);
		while (killerStream.read() != -1) ;
		
		outputStream.write(user.getUsername().getBytes());
		outputStream.flush();
		outputStream.write("\r\n".getBytes());
		outputStream.flush();
		
		
		killerStream = new ReadUntilMatchInputStream(inputStream, PASSWORD_PATTERN);
		while (killerStream.read() != -1) ;
		
		for (int i = 0; i < user.getPassword().length(); i++) {
			outputStream.write(user.getPassword().charAt(i));
			outputStream.flush();
		}
		
		outputStream.write("\r\n".getBytes());
		outputStream.flush();
		
		
		killerStream = new ReadUntilMatchInputStream(inputStream, PROMT_PATTERN);
		while (killerStream.read() != -1) ;
		
		//TODO: implement real result
		return true;
	}
	
}