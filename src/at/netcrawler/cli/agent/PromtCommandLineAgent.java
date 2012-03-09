package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLineInterface;
import at.andiwand.library.io.StreamUtil;
import at.andiwand.library.io.UnlimitedPushbackInputStream;
import at.andiwand.library.util.PatternUtil;
import at.andiwand.library.util.StringUtil;


public abstract class PromtCommandLineAgent extends CommandLineAgent {
	
	private static final String SYNCHRONIZE_COMMENT = "netCrawler-synchronize";
	private static final String RANDOM_SEPARATOR = " - ";
	
	private static class DefaultProcessFilter extends ProcessFilter {
		public DefaultProcessFilter(CommandLineInterface src)
				throws IOException {
			super(src);
		}
		
		@Override
		protected InputStream getFilterInputStream(InputStream in) {
			return in;
		}
		
		@Override
		protected OutputStream getFilterOutputStream(OutputStream out) {
			return out;
		}
	}
	
	private final Random random = new Random();
	
	protected final UnlimitedPushbackInputStream in;
	
	protected final Pattern promtPattern;
	protected final String commentPrefix;
	protected final String newLine;
	
	public PromtCommandLineAgent(CommandLineInterface cli,
			PromtCommandLineAgentSettings settings) throws IOException {
		super(cli, settings);
		
		this.in = new UnlimitedPushbackInputStream(super.in);
		this.promtPattern = settings.getPromtPattern();
		this.commentPrefix = settings.getCommentPrefix();
		this.newLine = settings.getNewLine();
		
		synchronizeStreams();
	}
	
	protected final Matcher readUntilMatch(Pattern pattern) throws IOException {
		return StreamUtil.readUntilMatch(in, pattern);
	}
	
	protected final Matcher readUntilFind(Pattern pattern) throws IOException {
		return StreamUtil.readUntilFind(in, pattern);
	}
	
	protected final String readLine() throws IOException {
		return StreamUtil.readLine(in);
	}
	
	protected final void flushLine() throws IOException {
		StreamUtil.flushLine(in);
	}
	
	protected final String prepareComment() {
		String randomString = "0x";
		randomString += StringUtil.fillFront(Integer.toHexString(random
				.nextInt()), '0', 8);
		return commentPrefix + SYNCHRONIZE_COMMENT + RANDOM_SEPARATOR
				+ randomString;
	}
	
	protected void synchronizeStreams() throws IOException {
		writer.write(newLine);
		writer.flush();
		
		readUntilFind(promtPattern);
		
		String comment = prepareComment();
		writer.write(comment);
		writer.write(newLine);
		writer.flush();
		
		readUntilFind(PatternUtil.getEndsWithPattern(comment));
		flushLine();
		readUntilFind(promtPattern);
	}
	
	protected void rawExecute(String command) throws IOException {
		writer.write(command);
		writer.write(newLine);
		writer.flush();
		
		flushLine();
	}
	
	protected abstract ProcessFilter getProcessFilter(String command,
			CommandLineInterface process);
	
	@Override
	public CommandLineInterface execute(String command) throws IOException {
		writer.write(command);
		writer.write(newLine);
		writer.flush();
		
		flushLine();
		
		CommandLineInterface process = new CommandLineProcess(in, out);
		process = getProcessFilter(command, process);
		return process;
	}
	
}