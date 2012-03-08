package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public abstract class GenericCLIAgentFactory<A extends CLIAgent, S extends CLIAgentSettings> extends
		CLIAgentFactory {
	
	@Override
	public abstract Class<A> getAgentClass();
	
	@Override
	public abstract Class<S> getSettingsClass();
	
	@Override
	@SuppressWarnings("unchecked")
	protected final A createAgentImpl(CommandLineInterface cli,
			CLIAgentSettings settings) throws IOException {
		return createAgentGenericImpl(cli, (S) settings);
	}
	
	protected abstract A createAgentGenericImpl(CommandLineInterface cli,
			S settings) throws IOException;
	
	@Override
	@SuppressWarnings("unchecked")
	protected final A createAgentImpl(CLISocket socket,
			CLIAgentSettings settings) throws IOException {
		return createAgentGenericImpl(socket, (S) settings);
	}
	
	protected abstract A createAgentGenericImpl(CLISocket socket, S settings)
			throws IOException;
	
}