package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public abstract class PromtCommandLineAgentFactory<A extends PromtCommandLineAgent, S extends PromtCommandLineAgentSettings>
		extends GenericCommandLineAgentFactory<A, S> {
	
	protected abstract A createAgentGenericImpl(CommandLineInterface cli,
			S settings) throws IOException;
	
}