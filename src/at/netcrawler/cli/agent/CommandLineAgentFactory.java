package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public abstract class CommandLineAgentFactory {
	
	public abstract Class<? extends CommandLineAgent> getAgentClass();
	
	public abstract Class<? extends CommandLineAgentSettings> getSettingsClass();
	
	public final CommandLineAgent createAgent(CommandLineInterface cli,
			CommandLineAgentSettings settings) throws IOException {
		if (!settings.getClass().equals(getSettingsClass()))
			throw new IllegalArgumentException("Illegal settings class");
		
		return createAgentImpl(cli, settings);
	}
	
	protected abstract CommandLineAgent createAgentImpl(CommandLineInterface cli,
			CommandLineAgentSettings settings) throws IOException;
	
}