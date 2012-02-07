package at.netcrawler.network.manager.cli;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import at.andiwand.library.network.ip.IPAddress;
import at.andiwand.library.util.QuickPattern;
import at.netcrawler.cli.agent.PromtPatternCLIAgent;
import at.netcrawler.network.Capability;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkInterface;


public class CiscoCLIDeviceManager extends CLIDeviceManager {
	
	private static final String IDENTICATION_COMMAND = "show version";
	private static final QuickPattern IDENTICATION_PATTERN = new QuickPattern(
			"^processor board id ((.*?)( \\((.*?)\\))?)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 2);
	
	private static final String HOSTNAME_COMMAND = "show running-config";
	private static final QuickPattern HOSTNAME_PATTERN = new QuickPattern(
			"^hostname (.*?)$", Pattern.MULTILINE, 1);
	
	private static final String SYSTEM_COMMAND = "show version";
	private static final QuickPattern SYSTEM_PATTERN = new QuickPattern(
			".*?, (.+?) software \\((.+?)\\).*", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 0);
	
	public CiscoCLIDeviceManager(NetworkDevice device,
			PromtPatternCLIAgent agent) {
		super(device, agent);
		
		addExtensionManager(new CiscoCLIExtensionManager());
	}
	
	public String getIdentication() throws IOException {
		return executeAndFind(IDENTICATION_COMMAND, IDENTICATION_PATTERN);
	}
	
	public String getHostname() throws IOException {
		return executeAndFind(HOSTNAME_COMMAND, HOSTNAME_PATTERN);
	}
	
	public String getSystem() throws IOException {
		return executeAndFind(SYSTEM_COMMAND, SYSTEM_PATTERN);
	}
	
	public long getUptime() throws IOException {
		// TODO: implement
		return -1;
	}
	
	public Set<Capability> getCapabilities() throws IOException {
		// TODO: implement
		return null;
	}
	
	@Override
	public Capability getMajorCapability() throws IOException {
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
		// TODO: implement
		return false;
	}
	
	@Override
	public Set<IPAddress> discoverNeighbors() {
		// TODO: implement
		return null;
	}
	
}