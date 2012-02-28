package at.netcrawler.test;

import java.io.IOException;
import java.util.List;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.cli.agent.CiscoCLIAgent;
import at.netcrawler.cli.agent.CiscoCLIAgentSettings;
import at.netcrawler.network.CDPNeighbor;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.LocalSSHConnection;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;
import at.netcrawler.network.manager.cli.CiscoCLIDeviceManager;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.extension.CiscoDeviceExtension;


public class CiscoCommandLineDeviceManagerTest {
	
	public static void main(String[] args) throws IOException {
		String addressString = "192.168.13.3";
		IPv4Address address = new IPv4Address(addressString);
		int port = 22;
		
		IPDeviceAccessor accessor = new IPDeviceAccessor(address);
		
		// TelnetSettings settings = new TelnetSettings();
		// settings.setPort(port);
		//
		// LocalTelnetConnection connection = new LocalTelnetConnection();
		
		SSHSettings settings = new SSHSettings();
		settings.setVersion(SSHVersion.VERSION2);
		settings.setPort(port);
		settings.setUsername("cisco");
		settings.setPassword("cisco");
		
		LocalSSHConnection connection = new LocalSSHConnection(accessor,
				settings);
		
		CiscoCLIAgentSettings agentSettings = new CiscoCLIAgentSettings();
		// agentSettings.setLogonUsername("cisco");
		// agentSettings.setLogonPassword("cisco");
		
		CiscoCLIAgent agent = new CiscoCLIAgent(connection, agentSettings);
		
		NetworkDevice device = new NetworkDevice();
		
		CiscoCLIDeviceManager deviceManager = new CiscoCLIDeviceManager(device,
				agent);
		deviceManager.complete();
		
		@SuppressWarnings("unchecked")
		List<CDPNeighbor> neighbors = (List<CDPNeighbor>) device
				.getValue(CiscoDeviceExtension.CDP_NEIGHBORS);
		for (CDPNeighbor neighbor : neighbors) {
			System.out.println(neighbor);
		}
		
		connection.close();
	}
	
}