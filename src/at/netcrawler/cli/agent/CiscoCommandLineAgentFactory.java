package at.netcrawler.cli.agent;

import java.io.IOException;

import at.andiwand.library.cli.CommandLineInterface;


public class CiscoCommandLineAgentFactory extends
		PromtCommandLineAgentFactory<CiscoCommandLineAgent, CiscoCommandLineAgentSettings> {
	
	@Override
	public Class<CiscoCommandLineAgent> getAgentClass() {
		return CiscoCommandLineAgent.class;
	}
	
	@Override
	public Class<CiscoCommandLineAgentSettings> getSettingsClass() {
		return CiscoCommandLineAgentSettings.class;
	}
	
	@Override
	protected CiscoCommandLineAgent createAgentGenericImpl(CommandLineInterface cli,
			CiscoCommandLineAgentSettings settings) throws IOException {
		return new CiscoCommandLineAgent(cli, settings);
	}
	
}