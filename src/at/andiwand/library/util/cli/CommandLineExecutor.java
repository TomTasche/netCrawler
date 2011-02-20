package at.andiwand.library.util.cli;

import java.io.IOException;



public abstract class CommandLineExecutor {
	
	protected CommandLine commandLine;
	
	
	public CommandLineExecutor(CommandLine commandLine) {
		this.commandLine = commandLine;
	}
	
	
	public abstract Command execute(String command) throws IOException;
	
	public void close() throws IOException {
		commandLine.close();
	}
	
}