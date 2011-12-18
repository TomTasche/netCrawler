package at.netcrawler.test;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.cli.agent.CiscoCommandLineAgent;
import at.netcrawler.cli.agent.CiscoCommandLineAgentSettings;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.LocalSSHConnection;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;


public class CiscoCommandLineAgentTest {
	
	public static void main(String[] args) throws Throwable {
		String addressString = "192.168.13.3";
		IPv4Address address = IPv4Address.getByAddress(addressString);
		int port = 22;
		
		IPDeviceAccessor accessor = new IPDeviceAccessor(address);
		
//		TelnetSettings settings = new TelnetSettings();
//		settings.setPort(port);
//		
//		LocalTelnetConnection connection = new LocalTelnetConnection();
		
		SSHSettings settings = new SSHSettings();
		settings.setVersion(SSHVersion.VERSION2);
		settings.setPort(port);
		settings.setUsername("cisco");
		settings.setPassword("cisco");
		
		LocalSSHConnection connection = new LocalSSHConnection();
		connection.connect(accessor, settings);
		
		CiscoCommandLineAgentSettings agentSettings = new CiscoCommandLineAgentSettings();
		
		CiscoCommandLineAgent agent = new CiscoCommandLineAgent(connection,
				agentSettings);
		System.out.println(agent.execute("show cdp neighbors detail"));
		
		connection.close();
	}
	
}