package at.netcrawler.cli.agent.deprecated;

import java.io.IOException;

import at.andiwand.library.cli.CommandLine;


public class LinuxPromptPatternAgent extends PromptPatternAgent {
	
	private static final String PROMT_PATTERN = "(.*?)@(.*?):(.*?)\\$";
	private static final String COMMENT_PREFIX = "#";
	
	public LinuxPromptPatternAgent(CommandLine commandLine) throws IOException {
		super(commandLine, PROMT_PATTERN, COMMENT_PREFIX);
		
		awaitPrompt();
	}
	
}