package at.netcrawler.test;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.cli.agent.CiscoCommandLineAgent;
import at.netcrawler.cli.agent.CiscoCommandLineAgentSettings;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.telnet.LocalTelnetConnection;
import at.netcrawler.network.connection.telnet.TelnetSettings;


public class CiscoCommandLineAgentTest {
	
	public static void main(String[] args) throws Throwable {
		String addressString = "192.168.15.101";
		IPv4Address address = new IPv4Address(addressString);
		int port = 23;
		
		IPDeviceAccessor accessor = new IPDeviceAccessor(address);
		
		TelnetSettings settings = new TelnetSettings();
		settings.setPort(port);
		
		LocalTelnetConnection connection = new LocalTelnetConnection(accessor,
				settings);
		
		// SSHSettings settings = new SSHSettings();
		// settings.setVersion(SSHVersion.VERSION2);
		// settings.setPort(port);
		// settings.setUsername("cisco");
		// settings.setPassword("cisco");
		//
		// LocalSSHConnection connection = new LocalSSHConnection(accessor,
		// settings);
		
		CiscoCommandLineAgentSettings agentSettings = new CiscoCommandLineAgentSettings();
		agentSettings.setLogonUsername("cisco");
		agentSettings.setLogonPassword("cisco");
		
		CiscoCommandLineAgent agent = new CiscoCommandLineAgent(connection,
				agentSettings);
		System.out.println(agent.executeAndRead("show version"));
		
		connection.close();
	}
	
}