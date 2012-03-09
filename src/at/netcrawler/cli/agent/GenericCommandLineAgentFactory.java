package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public abstract class GenericCommandLineAgentFactory<A extends CommandLineAgent, S extends CommandLineAgentSettings>
		extends CommandLineAgentFactory {
	
	@Override
	public abstract Class<A> getAgentClass();
	
	@Override
	public abstract Class<S> getSettingsClass();
	
	@Override
	@SuppressWarnings("unchecked")
	protected final A createAgentImpl(CommandLineInterface cli,
			CommandLineAgentSettings settings) throws IOException {
		return createAgentGenericImpl(
				cli, (S) settings);
	}
	
	protected abstract A createAgentGenericImpl(CommandLineInterface cli,
			S settings) throws IOException;
	
}