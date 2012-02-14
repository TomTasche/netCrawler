package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public abstract class PromtPatternCLIAgentFactory<S extends PromtPatternCLIAgentSettings> extends
		GenericCLIAgentFactory<S> {
	
	public PromtPatternCLIAgentFactory(Class<S> settingsClass) {
		super(settingsClass);
	}
	
	protected abstract PromtPatternCLIAgent createAgentGenericImpl(
			CommandLineInterface cli, S settings) throws IOException;
	
	protected abstract PromtPatternCLIAgent createAgentGenericImpl(
			CLISocket socket, S settings) throws IOException;
	
}