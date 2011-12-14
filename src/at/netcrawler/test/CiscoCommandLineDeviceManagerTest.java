package at.netcrawler.test;

import java.io.IOException;
import java.net.InetAddress;

import at.netcrawler.cli.agent.CiscoCommandLineAgent;
import at.netcrawler.cli.agent.CiscoCommandLineAgentSettings;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.telnet.LocalTelnetConnection;
import at.netcrawler.network.connection.telnet.TelnetSettings;
import at.netcrawler.network.manager.cli.CiscoCommandLineDeviceManager;
import at.netcrawler.network.model.NetworkDevice;


public class CiscoCommandLineDeviceManagerTest {
	
	public static void main(String[] args) throws IOException {
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
		
		NetworkDevice device = new NetworkDevice();
		
		CiscoCommandLineDeviceManager deviceManager = new CiscoCommandLineDeviceManager(
				device, agent);
		deviceManager.readDevice();
		System.out.println(device);
		
		connection.close();
	}
	
}