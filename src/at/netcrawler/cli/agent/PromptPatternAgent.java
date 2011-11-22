package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLine;
import at.andiwand.library.util.StreamUtil;
import at.netcrawler.stream.IgnoreFirstLineInputStream;
import at.netcrawler.stream.ReadAfterMatchInputStream;
import at.netcrawler.stream.ReadUntilMatchInputStream;


public class PromptPatternAgent extends CommandLineAgent {
	
	public static final String DEFAULT_KILLER_PREFIX = "-netCrawler-";
	
	private Pattern promtPattern;
	
	private String commentPrefix;
	private String killerPrefix;
	private int killerNumber;
	
	public PromptPatternAgent(CommandLine commandLine, Pattern promtPattern,
			String commentPrefix) {
		this(commandLine, promtPattern, commentPrefix, DEFAULT_KILLER_PREFIX);
	}
	public PromptPatternAgent(CommandLine commandLine, Pattern promtPattern,
			String commentPrefix, String killerPrefix) {
		super(commandLine);
		
		this.promtPattern = promtPattern;
		this.commentPrefix = commentPrefix;
		this.killerPrefix = killerPrefix;
	}
	
	@Override
	public synchronized CommandLineProcess execute(String command)
			throws IOException {
		synchronizeStreams();
		
		InputStream inputStream = commandLine.getInputStream();
		OutputStream outputStream = commandLine.getOutputStream();
		
		Pattern commandPattern = Pattern.compile(".*" + command);
		
		outputStream.write(command.getBytes());
		outputStream.write("\n".getBytes());
		outputStream.flush();
		
		inputStream = new ReadAfterMatchInputStream(inputStream, commandPattern);
		inputStream = new IgnoreFirstLineInputStream(inputStream);
		inputStream = commandChainHook(inputStream);
		inputStream = new ReadUntilMatchInputStream(inputStream, promtPattern);
		
		return new CommandLineProcess(inputStream, outputStream);
	}
	
	protected InputStream commandChainHook(InputStream inputStream) {
		return inputStream;
	}
	
	public synchronized void synchronizeStreams() throws IOException {
		InputStream inputStream = commandLine.getInputStream();
		OutputStream outputStream = commandLine.getOutputStream();
		
		String killerString = commentPrefix + killerPrefix + (killerNumber++);
		Pattern killerPattern = Pattern.compile(killerString, Pattern.LITERAL);
		
		outputStream.write(killerString.getBytes());
		outputStream.write("\n".getBytes());
		outputStream.flush();
		
		InputStream killerInputStream = new ReadUntilMatchInputStream(
				inputStream, killerPattern);
		StreamUtil.killStream(killerInputStream);
	}
	
}