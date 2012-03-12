package at.netcrawler.network.manager.cli;

import java.io.IOException;

import at.netcrawler.cli.agent.CiscoCommandLineAgent;
import at.netcrawler.cli.agent.CiscoCommandLineAgentSettings;
import at.netcrawler.network.connection.CommandLineConnection;
import at.netcrawler.network.manager.GenericDeviceManagerFactory;
import at.netcrawler.network.model.NetworkDevice;


// TODO: improve (not cisco only)
public class CommandLineDeviceManagerFactory extends
		GenericDeviceManagerFactory<CommandLineDeviceManager, CommandLineConnection> {
	
	@Override
	public CommandLineDeviceManager buildDeviceManagerGeneric(
			NetworkDevice device, CommandLineConnection connection)
			throws IOException {
		CiscoCommandLineAgentSettings settings = new CiscoCommandLineAgentSettings();
		CiscoCommandLineAgent agent = new CiscoCommandLineAgent(connection,
				settings);
		return new CiscoCommandLineDeviceManager(device, agent);
	}
	
}