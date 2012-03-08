package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public class CiscoCLIAgentFactory extends
		PromtPatternCLIAgentFactory<CiscoCLIAgent, CiscoCLIAgentSettings> {
	
	@Override
	public Class<CiscoCLIAgent> getAgentClass() {
		return CiscoCLIAgent.class;
	}
	
	@Override
	public Class<CiscoCLIAgentSettings> getSettingsClass() {
		return CiscoCLIAgentSettings.class;
	}
	
	@Override
	protected CiscoCLIAgent createAgentGenericImpl(CommandLineInterface cli,
			CiscoCLIAgentSettings settings) throws IOException {
		return new CiscoCLIAgent(cli, settings);
	}
	
	@Override
	protected CiscoCLIAgent createAgentGenericImpl(CLISocket socket,
			CiscoCLIAgentSettings settings) throws IOException {
		return new CiscoCLIAgent(socket, settings);
	}
	
}