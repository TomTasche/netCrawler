package util.cli.agent;

import util.cli.CommandLineInterface;


public abstract class CommandLineInterfaceAgent {
	
	protected CommandLineInterface cli;
	
	
	public CommandLineInterfaceAgent(CommandLineInterface cli) {
		this.cli = cli;
	}
	
	
	public CommandLineInterface getCLI() {
		return cli;
	}
	
	public abstract String getPrompt();
	
}