package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public class LinuxCLIAgentFactory extends
		PromtPatternCLIAgentFactory<LinuxCLIAgentSettings> {
	
	public LinuxCLIAgentFactory() {
		super(LinuxCLIAgentSettings.class);
	}
	
	@Override
	public Class<LinuxCLIAgent> getAgentClass() {
		return LinuxCLIAgent.class;
	}
	
	@Override
	protected LinuxCLIAgent createAgentGenericImpl(CommandLineInterface cli,
			LinuxCLIAgentSettings settings) throws IOException {
		return new LinuxCLIAgent(cli, settings);
	}
	
	@Override
	protected LinuxCLIAgent createAgentGenericImpl(CLISocket socket,
			LinuxCLIAgentSettings settings) throws IOException {
		return new LinuxCLIAgent(socket, settings);
	}
	
}