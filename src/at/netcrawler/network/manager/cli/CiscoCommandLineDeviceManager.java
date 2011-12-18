package at.netcrawler.network.manager.cli;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import at.andiwand.library.network.ip.IPAddress;
import at.netcrawler.cli.agent.CommandLineAgent;
import at.netcrawler.network.Capability;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkInterface;


public class CiscoCommandLineDeviceManager extends CommandLineDeviceManager {
	
	private static final String IDENTICATION_COMMAND = "show version";
	private static final Pattern IDENTICATION_PATTERN = Pattern.compile(
			"^processor board id ((.*?)( \\((.*?)\\))?)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE);
	private static final int IDENTICATION_GROUP = 2;
	
	private static final String HOSTNAME_COMMAND = "show running-config";
	private static final Pattern HOSTNAME_PATTERN = Pattern.compile(
			"^hostname (.*?)$", Pattern.MULTILINE);
	private static final int HOSTNAME_GROUP = 1;
	
	private static final String SYSTEM_COMMAND = "show version";
	private static final Pattern SYSTEM_PATTERN = Pattern.compile(
			".*?, (.+?) software \\((.+?)\\).*", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE);
	private static final int SYSTEM_GROUP = 0;
	
	public CiscoCommandLineDeviceManager(NetworkDevice device,
			CommandLineAgent agent) {
		super(device, agent);
		
		addExtensionManager(new CiscoCommandLineExtensionManager());
	}
	
	public String getIdentication() throws IOException {
		return executeAndFind(IDENTICATION_COMMAND, IDENTICATION_PATTERN,
				IDENTICATION_GROUP);
	}
	
	public String getHostname() throws IOException {
		return executeAndFind(HOSTNAME_COMMAND, HOSTNAME_PATTERN,
				HOSTNAME_GROUP);
	}
	
	public String getSystem() throws IOException {
		return executeAndFind(SYSTEM_COMMAND, SYSTEM_PATTERN, SYSTEM_GROUP);
	}
	
	public long getUptime() throws IOException {
		// TODO: implement
		return -1;
	}
	
	public Set<Capability> getCapabilities() throws IOException {
		// TODO: implement
		return null;
	}
	
	public Set<NetworkInterface> getInterfaces() throws IOException {
		// TODO: implement
		return null;
	}
	
	public Set<IPAddress> getManagementAddresses() throws IOException {
		// TODO: implement
		return null;
	}
	
	public boolean setHostname(String hostname) throws IOException {
		throw new UnsupportedOperationException();
	}
	
}