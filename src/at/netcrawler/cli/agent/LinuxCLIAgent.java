package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public class LinuxCLIAgent extends PromtCommandLineAgent {
	
	public LinuxCLIAgent(CommandLineInterface cli,
			LinuxCLIAgentSettings settings) throws IOException {
		super(cli, settings);
		
		handleLogin(settings);
	}
	
	public LinuxCLIAgent(CLISocket socket, LinuxCLIAgentSettings settings)
			throws IOException {
		super(socket, settings);
		
		handleLogin(settings);
	}
	
	// TODO: implement
	private void handleLogin(LinuxCLIAgentSettings settings) {
		
	}
	
}