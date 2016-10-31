package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public class LinuxCLIAgentFactory extends
		PromptCommandLineAgentFactory<LinuxCommandLineAgent, LinuxCLIAgentSettings> {
	
	@Override
	public Class<LinuxCommandLineAgent> getAgentClass() {
		return LinuxCommandLineAgent.class;
	}
	
	@Override
	public Class<LinuxCLIAgentSettings> getSettingsClass() {
		return LinuxCLIAgentSettings.class;
	}
	
	@Override
	protected LinuxCommandLineAgent createAgentGenericImpl(
			CommandLineInterface cli, LinuxCLIAgentSettings settings)
			throws IOException {
		return new LinuxCommandLineAgent(cli, settings);
	}
	
}