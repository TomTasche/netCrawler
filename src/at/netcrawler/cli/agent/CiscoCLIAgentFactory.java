package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public class CiscoCLIAgentFactory extends
		PromtPatternCLIAgentFactory<CiscoCLIAgentSettings> {
	
	public CiscoCLIAgentFactory() {
		super(CiscoCLIAgentSettings.class);
	}
	
	@Override
	public Class<CiscoCLIAgent> getAgentClass() {
		return CiscoCLIAgent.class;
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