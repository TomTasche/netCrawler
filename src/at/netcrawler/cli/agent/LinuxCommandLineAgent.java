package at.netcrawler.cli.agent;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

import at.andiwand.library.cli.CommandLine;


public class LinuxCommandLineAgent extends CommandLineAgent {
	
	private static final String COMMENT_PREFIX = "#";
	private static final Pattern PROMT_PATTERN = Pattern
			.compile("(.*?)@(.*?):(.*?)\\$");
	private static final Charset CHARSET = Charset.forName("UTF-8");
	
	public LinuxCommandLineAgent(CommandLine commandLine) {
		super(commandLine, CHARSET, PROMT_PATTERN, COMMENT_PREFIX);
	}
	
}