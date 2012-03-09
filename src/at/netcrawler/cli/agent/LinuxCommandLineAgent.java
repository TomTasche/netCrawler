package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public class LinuxCommandLineAgent extends PromtCommandLineAgent {
	
	public LinuxCommandLineAgent(CommandLineInterface cli,
			LinuxCLIAgentSettings settings) throws IOException {
		super(cli, settings);
		
		handleLogin(settings);
	}
	
	// TODO: implement
	private void handleLogin(LinuxCLIAgentSettings settings) {
		
	}
	
}