package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLine;


public abstract class CommandLineAgent {
	
	protected final CommandLine commandLine;
	
	public CommandLineAgent(CommandLine commandLine) {
		this.commandLine = commandLine;
	}
	
	public String readExecute(String command) throws IOException {
		return execute(command).readInput();
	}
	
	public abstract CommandLineProcess execute(String command)
			throws IOException;
	
}