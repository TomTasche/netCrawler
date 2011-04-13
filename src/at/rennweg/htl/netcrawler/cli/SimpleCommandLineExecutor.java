package at.rennweg.htl.netcrawler.cli;

import java.io.IOException;

import at.andiwand.library.util.cli.CommandLine;
import at.rennweg.htl.netcrawler.cli.executor.SimpleRemoteExecutor;



public abstract class SimpleCommandLineExecutor implements SimpleRemoteExecutor {
	
	protected CommandLine commandLine;
	
	
	public SimpleCommandLineExecutor(CommandLine commandLine) {
		this.commandLine = commandLine;
	}
	
	
	public abstract String execute(String command) throws IOException;
	
	public void close() throws IOException {
		commandLine.close();
	}
	
}