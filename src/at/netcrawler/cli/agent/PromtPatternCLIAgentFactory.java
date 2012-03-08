package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public abstract class PromtPatternCLIAgentFactory<A extends PromtPatternCLIAgent, S extends PromtPatternCLIAgentSettings> extends
		GenericCLIAgentFactory<A, S> {
	
	protected abstract A createAgentGenericImpl(CommandLineInterface cli,
			S settings) throws IOException;
	
	protected abstract A createAgentGenericImpl(CLISocket socket, S settings)
			throws IOException;
	
}