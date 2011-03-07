package at.rennweg.htl.netcrawler.network.agent.cli;

import at.andiwand.library.util.cli.CommandLine;
import at.rennweg.htl.netcrawler.network.agent.CiscoDeviceAgent;


public abstract class CiscoDeviceCLIAgent extends CiscoDeviceAgent {
	
	protected CommandLine commandLine;
	
	
	public CiscoDeviceCLIAgent(CommandLine commandLine) {
		this.commandLine = commandLine;
	}
	
}