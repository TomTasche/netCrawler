package at.netcrawler.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.cli.CommandLine;
import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.cli.agent.CiscoCommandLineAgent;
import at.netcrawler.cli.agent.CiscoCommandLineAgentSettings;
import at.netcrawler.network.CDPNeighbors;
import at.netcrawler.network.accessor.DeviceAccessor;
import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ConnectionSettings;
import at.netcrawler.network.connection.ssh.LocalSSHConnection;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;
import at.netcrawler.network.connection.telnet.LocalTelnetConnection;
import at.netcrawler.network.connection.telnet.TelnetSettings;
import at.netcrawler.network.manager.cli.CiscoCommandLineDeviceManager;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.extension.CiscoExtension;


public class SimpleSchoolCrawler {
	
	public static class Logon {
		public String username;
		public String password;
	}
	
	public static void main(String[] args) throws Throwable {
		String rootAddressString = "192.168.0.254";
		IPv4Address rootAddress = IPv4Address.getByAddress(rootAddressString);
		
		Logon logon = new Logon();
		logon.username = "cisco";
		logon.password = "cisco";
		
		Set<String> usedIDs = new HashSet<String>();
		
		crawlDevice(rootAddress, logon, usedIDs);
	}
	
	public static void crawlDevice(IPv4Address address, Logon logon,
			Set<String> usedIDs) throws IOException {
		DeviceAccessor accessor = new IPDeviceAccessor(address);
		ConnectionSettings settings;
		CommandLine commandLine = null;
		
		for (int i = 0; i < 3; i++) {
			settings = generateSettings(i, logon);
			
			try {
				commandLine = openConnection(accessor, settings);
			} catch (IOException e) {}
		}
		
		if (commandLine == null) throw new IOException("not able to connect!");
		
		CiscoCommandLineAgentSettings agentSettings = new CiscoCommandLineAgentSettings();
		agentSettings.setLogonUsername(logon.username);
		agentSettings.setLogonPassword(logon.password);
		
		CiscoCommandLineAgent agent = new CiscoCommandLineAgent(commandLine,
				agentSettings);
		
		NetworkDevice device = new NetworkDevice();
		CiscoCommandLineDeviceManager deviceManager = new CiscoCommandLineDeviceManager(
				device, agent);
		
		String id = deviceManager.getIdentication();
		if (usedIDs.contains(id)) return;
		
		deviceManager.readDevice();
		
		System.out.println("hostname:		"
				+ device.getValue(NetworkDevice.HOSTNAME));
		System.out.println("model number:	"
				+ device.getValue(CiscoExtension.MODEL_NUMBER));
		System.out.println("serial number:	"
				+ device.getValue(CiscoExtension.SYSTEM_SERIAL_NUMBER));
		System.out.println("processor:		"
				+ device.getValue(CiscoExtension.PROCESSOR_STRING));
		System.out.println();
		
		usedIDs.add(id);
		
		CDPNeighbors neighbors = (CDPNeighbors) device.getValue(CiscoExtension.CDP_NEIGHBORS);
		for (CDPNeighbors.Neighbor neighbor : neighbors) {
			IPv4Address neighborAddress = neighbor.getManagementAddresses().iterator().next();
			
			crawlDevice(neighborAddress, logon, usedIDs);
		}
	}
	
	public static ConnectionSettings generateSettings(int i, Logon logon) {
		switch (i) {
		case 0:
		case 1:
			SSHSettings sshSettings = new SSHSettings();
			
			if (i == 0) sshSettings.setVersion(SSHVersion.VERSION2);
			else sshSettings.setVersion(SSHVersion.VERSION1);
			sshSettings.setUsername(logon.username);
			sshSettings.setPassword(logon.password);
			
			return sshSettings;
		case 2:
			TelnetSettings telnetSettings = new TelnetSettings();
			
			return telnetSettings;
			
		default:
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public static CommandLine openConnection(DeviceAccessor accessor,
			ConnectionSettings settings) throws IOException {
		if (settings.getClass().equals(SSHSettings.class)) {
			LocalSSHConnection connection = new LocalSSHConnection();
			connection.connect(accessor, settings);
			return connection;
		} else if (settings.getClass().equals(TelnetSettings.class)) {
			LocalTelnetConnection connection = new LocalTelnetConnection();
			connection.connect(accessor, settings);
			return connection;
		}
		
		throw new IllegalStateException("Unreachable section");
	}
}