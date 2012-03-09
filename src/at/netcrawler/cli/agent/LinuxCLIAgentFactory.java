package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public class LinuxCLIAgentFactory extends
		PromtCommandLineAgentFactory<LinuxCLIAgent, LinuxCLIAgentSettings> {
	
	@Override
	public Class<LinuxCLIAgent> getAgentClass() {
		return LinuxCLIAgent.class;
	}
	
	@Override
	public Class<LinuxCLIAgentSettings> getSettingsClass() {
		return LinuxCLIAgentSettings.class;
	}
	
	@Override
	protected LinuxCLIAgent createAgentGenericImpl(CommandLineInterface cli,
			LinuxCLIAgentSettings settings) throws IOException {
		return new LinuxCLIAgent(cli, settings);
	}
	
}