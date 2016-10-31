package at.netcrawler.test;

import java.io.IOException;
import java.util.List;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.cli.agent.CiscoCommandLineAgent;
import at.netcrawler.cli.agent.CiscoCommandLineAgentSettings;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.LocalSSHConnection;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;
import at.netcrawler.network.manager.cli.CiscoCommandLineDeviceManager;
import at.netcrawler.network.model.CDPNeighbor;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.extension.CiscoDeviceExtension;


public class CiscoCommandLineDeviceManagerTest {
	
	public static void main(String[] args) throws IOException {
		String addressString = "192.168.1.2";
		IPv4Address address = new IPv4Address(addressString);
		
		IPDeviceAccessor accessor = new IPDeviceAccessor(address);
		
		// TelnetSettings settings = new TelnetSettings();
		//
		// LocalTelnetConnection connection = new LocalTelnetConnection();
		
		SSHSettings settings = new SSHSettings();
		settings.setVersion(SSHVersion.VERSION2);
		settings.setUsername("cisco");
		settings.setPassword("cisco");
		
		LocalSSHConnection connection = new LocalSSHConnection(accessor,
				settings);
		
		CiscoCommandLineAgentSettings agentSettings = new CiscoCommandLineAgentSettings();
		// agentSettings.setLogonUsername("cisco");
		// agentSettings.setLogonPassword("cisco");
		
		CiscoCommandLineAgent agent = new CiscoCommandLineAgent(connection,
				agentSettings);
		
		NetworkDevice device = new NetworkDevice();
		
		CiscoCommandLineDeviceManager deviceManager = new CiscoCommandLineDeviceManager(
				device, agent);
		deviceManager.complete();
		
		System.out.println(device.getValue(NetworkDevice.HOSTNAME));
		System.out.println(device.getValue(NetworkDevice.MAJOR_CAPABILITY));
		System.out.println(device.getValue(NetworkDevice.SYSTEM_DESCRIPTION));
		System.out.println(device.getValue(NetworkDevice.MANAGEMENT_ADDRESSES));
		
		@SuppressWarnings("unchecked")
		List<CDPNeighbor> neighbors = (List<CDPNeighbor>) device
				.getValue(CiscoDeviceExtension.CDP_NEIGHBORS);
		for (CDPNeighbor neighbor : neighbors) {
			System.out.println(neighbor);
		}
		
		connection.close();
	}
	
}