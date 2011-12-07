package at.netcrawler.cli.agent.deprecated;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLine;
import at.andiwand.library.io.StreamUtil;
import at.andiwand.library.io.TeeInputStream;
import at.andiwand.library.util.PatternUtil;
import at.netcrawler.io.deprecated.IgnoreFirstLineInputStream;
import at.netcrawler.io.deprecated.ReadAfterMatchInputStream;
import at.netcrawler.io.deprecated.ReadUntilMatchInputStream;


public class PromptPatternAgent extends CommandLineAgent {
	
	private static final String ENTER = "\n";
	private static final String ANYTHING = ".*";
	
	public static final String DEFAULT_CHARSET = "US-ASCII";
	public static final String DEFAULT_KILLER_PREFIX = "_netcrawler_";
	public static final String DEFAULT_RANDOM_SEPARATOR = "_";
	
	protected final InputStream inputStream;
	protected final OutputStream outputStream;
	
	protected final Charset charset;
	protected final byte[] enter;
	
	private final String promptPattern;
	private final String commentPrefix;
	private final String killerPrefix;
	private int killerNumber;
	
	private final Random random = new Random();
	private final String randomSeparator;
	
	public PromptPatternAgent(CommandLine commandLine, String promptPattern,
			String commentPrefix) {
		this(commandLine, promptPattern, commentPrefix, DEFAULT_KILLER_PREFIX);
	}
	
	public PromptPatternAgent(CommandLine commandLine, String promptPattern,
			String commentPrefix, String killerPrefix) {
		this(commandLine, DEFAULT_CHARSET, promptPattern, commentPrefix,
				killerPrefix, DEFAULT_RANDOM_SEPARATOR);
	}
	
	public PromptPatternAgent(CommandLine commandLine, Charset charset,
			String promptPattern, String commentPrefix, String killerPrefix,
			String randomSeparator) {
		super(commandLine);
		
		try {
			inputStream = commandLine.getInputStream();
			outputStream = commandLine.getOutputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		this.charset = charset;
		this.enter = ENTER.getBytes(charset);
		this.promptPattern = promptPattern;
		this.commentPrefix = commentPrefix;
		this.killerPrefix = killerPrefix;
		this.randomSeparator = randomSeparator;
	}
	
	public PromptPatternAgent(CommandLine commandLine, String charset,
			String promptPattern, String commentPrefix, String killerPrefix,
			String randomSeperator) {
		this(commandLine, Charset.forName(charset), promptPattern,
				commentPrefix, killerPrefix, randomSeperator);
	}
	
	@Override
	public synchronized CommandLineProcess execute(String command)
			throws IOException {
		synchronizeStreams();
		
		Pattern commandPattern = Pattern.compile(ANYTHING
				+ PatternUtil.escapeString(command));
		Pattern promptPattern = Pattern.compile(ANYTHING + this.promptPattern);
		
		outputStream.write(command.getBytes(charset));
		outputStream.write(enter);
		outputStream.flush();
		
		InputStream inputStream = new ReadAfterMatchInputStream(
				this.inputStream, commandPattern);
		inputStream = new IgnoreFirstLineInputStream(inputStream);
		inputStream = commandChainHook(inputStream);
		inputStream = new TeeInputStream(inputStream, System.out);
		inputStream = new ReadUntilMatchInputStream(inputStream, promptPattern);
		
		return new CommandLineProcess(inputStream, outputStream, charset);
	}
	
	protected InputStream commandChainHook(InputStream inputStream) {
		return inputStream;
	}
	
	public synchronized void synchronizeStreams() throws IOException {
		String killerString = commentPrefix + killerPrefix + (killerNumber++)
				+ randomSeparator + random.nextInt();
		Pattern killerPattern = Pattern.compile(ANYTHING
				+ PatternUtil.escapeString(killerString));
		Pattern promptPattern = Pattern.compile(ANYTHING + this.promptPattern);
		
		outputStream.write(killerString.getBytes(charset));
		outputStream.write(enter);
		outputStream.flush();
		
		InputStream killerInputStream = new ReadUntilMatchInputStream(
				inputStream, killerPattern);
		StreamUtil.killStream(killerInputStream);
		
		killerInputStream = new ReadUntilMatchInputStream(inputStream,
				promptPattern);
		StreamUtil.killStream(killerInputStream);
	}
	
	public synchronized void awaitPrompt() throws IOException {
		Pattern promptPattern = Pattern.compile(ANYTHING + this.promptPattern);
		
		InputStream killerInputStream = new ReadUntilMatchInputStream(
				inputStream, promptPattern);
		StreamUtil.killStream(killerInputStream);
	}
	
}