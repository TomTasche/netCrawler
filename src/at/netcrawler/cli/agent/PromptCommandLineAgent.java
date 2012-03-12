package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLineInterface;
import at.andiwand.library.io.StreamUtil;
import at.andiwand.library.io.UnlimitedPushbackInputStream;
import at.andiwand.library.util.PatternUtil;
import at.andiwand.library.util.StringUtil;
import at.netcrawler.io.UntilLineMatchInputStream;


public abstract class PromptCommandLineAgent extends CommandLineAgent {
	
	private static final String SYNCHRONIZE_COMMENT = "netCrawler-synchronize";
	private static final String RANDOM_SEPARATOR = "-";
	
	private static class ProcessTerminatorInputStream extends
			UntilLineMatchInputStream {
		private final ProcessTerminator processFilter;
		
		public ProcessTerminatorInputStream(ProcessTerminator processFilter,
				InputStream in, Pattern promtPattern) {
			super(in, promtPattern);
			
			this.processFilter = processFilter;
		}
		
		// TODO: improve (workaround)
		@Override
		protected boolean match() throws IOException {
			if (available() > 0) return false;
			processFilter.terminate();
			return true;
		}
	}
	
	private class DefaultProcessTerminator extends ProcessTerminator {
		public DefaultProcessTerminator(CommandLineInterface src)
				throws IOException {
			super(src);
		}
		
		@Override
		protected InputStream getFilterInputStream(InputStream in) {
			return new ProcessTerminatorInputStream(this, in, promtPattern);
		}
	}
	
	private final Random random = new Random();
	
	protected final UnlimitedPushbackInputStream in;
	
	protected final Pattern promtPattern;
	protected final String commentPrefix;
	protected final String newLine;
	
	private ProcessTerminator lastProcessFilter;
	
	public PromptCommandLineAgent(CommandLineInterface cli,
			PromptCommandLineAgentSettings settings) throws IOException {
		super(cli, settings);
		
		this.in = new UnlimitedPushbackInputStream(super.in);
		this.promtPattern = settings.getPromtPattern();
		this.commentPrefix = settings.getCommentPrefix();
		this.newLine = settings.getNewLine();
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
	
	protected CommandLineInterface getProcessFilter(String command,
			CommandLineInterface process) throws IOException {
		return process;
	}
	
	protected ProcessTerminator getProcessTerminator(String command,
			CommandLineInterface process) throws IOException {
		return new DefaultProcessTerminator(process);
	}
	
	@Override
	public CommandLineInterface execute(String command) throws IOException {
		if (lastProcessFilter != null) {
			try {
				lastProcessFilter.waitFor();
			} catch (InterruptedException e) {
				throw new IOException("Termination await was interrupted", e);
			}
		}
		
		writer.write(command);
		writer.write(newLine);
		writer.flush();
		
		flushLine();
		
		CommandLineInterface process = new CommandLineProcess(in, out);
		process = lastProcessFilter = getProcessTerminator(command, process);
		process = getProcessFilter(command, process);
		return process;
	}
	
}