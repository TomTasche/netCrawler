package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.Reader;
import java.util.Random;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLineInterface;
import at.andiwand.library.util.PatternUtil;
import at.andiwand.library.util.StringUtil;
import at.netcrawler.io.FilterLastLineReader;
import at.netcrawler.io.UntilLineMatchReader;


public abstract class PromtPatternCLIAgent extends CLIAgent {
	
	private static final String SYNCHRONIZE_COMMENT = "netCrawler-synchronize";
	private static final String COMMENT_SUFFIX_SEPARATOR = " - ";
	
	protected final Pattern promtPattern;
	protected final String commentPrefix;
	protected final String newLine;
	
	private final Random random = new Random();
	
	public PromtPatternCLIAgent(CommandLineInterface cli,
			PromtPatternCLIAgentSettings settings) throws IOException {
		super(cli, settings);
		
		promtPattern = settings.getPromtPattern();
		commentPrefix = settings.getCommentPrefix();
		newLine = settings.getNewLine();
		
		synchronizeStreams();
	}
	
	public PromtPatternCLIAgent(CLISocket socket,
			PromtPatternCLIAgentSettings settings) throws IOException {
		super(socket, settings);
		
		promtPattern = settings.getPromtPattern();
		commentPrefix = settings.getCommentPrefix();
		newLine = settings.getNewLine();
		
		synchronizeStreams();
	}
	
	private String prepareComment(String string) {
		int suffixRandom = random.nextInt();
		String suffix = "0x"
				+ StringUtil.fillFront(Integer.toHexString(suffixRandom), '0',
						8);
		return commentPrefix + string + COMMENT_SUFFIX_SEPARATOR + suffix;
	}
	
	protected void synchronizeStreams() throws IOException {
		String comment = prepareComment(SYNCHRONIZE_COMMENT);
		
		out.write(newLine);
		out.flush();
		
		find(promtPattern);
		
		out.write(comment + newLine);
		out.flush();
		
		find(PatternUtil.endsWithPattern(comment));
		flushLine();
		find(promtPattern);
	}
	
	@Override
	protected void rawExecute(String command) throws IOException {
		out.write(command + newLine);
		out.flush();
		
		flushLine();
	}
	
	@Override
	protected CLIProcessTerminator getCLIProcessTerminator(String command) {
		return new CLIProcessTerminator() {
			protected Reader hookReader(Reader reader) {
				return new UntilLineMatchReader(reader, promtPattern) {
					protected void match() {
						terminate();
					}
				};
			}
		};
	}
	
	@Override
	protected CLISocketHook getCLISocketHook(String command) {
		return new CLISocketHook() {
			@Override
			protected Reader hookReader(Reader reader) {
				return new FilterLastLineReader(reader);
			}
		};
	}
}