package at.netcrawler.test;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.cli.agent.CiscoCLIAgent;
import at.netcrawler.cli.agent.CiscoCLIAgentSettings;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.LocalSSHConnection;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;


public class CiscoCommandLineAgentTest {
	
	public static void main(String[] args) throws Throwable {
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
		
		CiscoCLIAgent agent = new CiscoCLIAgent(connection, agentSettings);
		System.out.println(agent.execute("show cdp neighbors detail"));
		
		connection.close();
	}
	
}