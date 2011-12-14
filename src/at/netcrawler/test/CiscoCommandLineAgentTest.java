package at.netcrawler.test;

import java.net.InetAddress;

import at.netcrawler.cli.agent.CiscoCommandLineAgent;
import at.netcrawler.cli.agent.CiscoCommandLineAgentSettings;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.telnet.LocalTelnetConnection;
import at.netcrawler.network.connection.telnet.TelnetSettings;


public class CiscoCommandLineAgentTest {
	
	public static void main(String[] args) throws Throwable {
		InetAddress inetAddress = InetAddress.getLocalHost();
		int port = 54321;
		
		IPDeviceAccessor accessor = new IPDeviceAccessor(inetAddress);
		TelnetSettings settings = new TelnetSettings();
		settings.setPort(port);
		
		final LocalTelnetConnection connection = new LocalTelnetConnection();
		connection.connect(accessor, settings);
		
		CiscoCommandLineAgentSettings agentSettings = new CiscoCommandLineAgentSettings();
		agentSettings.setLogonUsername("cisco");
		agentSettings.setLogonPassword("cisco");
		
		CiscoCommandLineAgent agent = new CiscoCommandLineAgent(connection,
				agentSettings);
		System.out.println(agent.execute("show cdp neighbors detail"));
		
		connection.close();
	}
	
}