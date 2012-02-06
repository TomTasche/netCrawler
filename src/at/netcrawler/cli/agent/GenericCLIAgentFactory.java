package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public abstract class GenericCLIAgentFactory<S extends CLIAgentSettings> extends
		CLIAgentFactory {
	
	private final Class<S> settingsClass;
	
	public GenericCLIAgentFactory(Class<S> settingsClass) {
		this.settingsClass = settingsClass;
	}
	
	@Override
	public final Class<S> getSettingsClass() {
		return settingsClass;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected final CLIAgent createAgentImpl(CommandLineInterface cli,
			CLIAgentSettings settings) throws IOException {
		return createAgentGenericImpl(cli, (S) settings);
	}
	
	protected abstract CLIAgent createAgentGenericImpl(
			CommandLineInterface cli, S settings) throws IOException;
	
	@Override
	@SuppressWarnings("unchecked")
	protected final CLIAgent createAgentImpl(CLISocket socket,
			CLIAgentSettings settings) throws IOException {
		return createAgentGenericImpl(socket, (S) settings);
	}
	
	protected abstract CLIAgent createAgentGenericImpl(CLISocket socket,
			S settings) throws IOException;
	
}