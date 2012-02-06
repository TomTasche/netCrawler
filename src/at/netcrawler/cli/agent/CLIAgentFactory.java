package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public abstract class CLIAgentFactory {
	
	public abstract Class<? extends CLIAgent> getAgentClass();
	
	public abstract Class<? extends CLIAgentSettings> getSettingsClass();
	
	public final CLIAgent createAgent(CommandLineInterface cli,
			CLIAgentSettings settings) throws IOException {
		if (!settings.getClass().equals(getSettingsClass()))
			throw new IllegalArgumentException("Illegal settings class!");
		
		return createAgentImpl(cli, settings);
	}
	
	protected abstract CLIAgent createAgentImpl(CommandLineInterface cli,
			CLIAgentSettings settings) throws IOException;
	
	public final CLIAgent createAgent(CLISocket socket,
			CLIAgentSettings settings) throws IOException {
		if (!settings.getClass().equals(getSettingsClass()))
			throw new IllegalArgumentException("Illegal settings class!");
		
		return createAgentImpl(socket, settings);
	}
	
	protected abstract CLIAgent createAgentImpl(CLISocket socket,
			CLIAgentSettings settings) throws IOException;
	
}