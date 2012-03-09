package at.netcrawler.network.manager.cli;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import at.andiwand.library.network.ip.IPAddress;
import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.util.QuickPattern;
import at.netcrawler.DeviceSystem;
import at.netcrawler.cli.agent.PromtCommandLineAgent;
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
	
	// TODO: implement
	private static final String UPTIME_COMMAND = "show version";
	private static final QuickPattern UPTIME_PATTERN = new QuickPattern(
			".*?, (.+?) software \\((.+?)\\).*", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 0);
	
	public CiscoCLIDeviceManager(NetworkDevice device,
			PromtCommandLineAgent agent) {
		super(device, agent);
		
		addExtensionManager(new CiscoCLIDeviceExtensionManager());
		addExtensionManager(new CiscoCLISwitchExtensionManager());
		addExtensionManager(new CiscoCLIRouterExtensionManager());
	}
	
	protected String getIdentication() throws IOException {
		return executeAndFind(IDENTICATION_COMMAND, IDENTICATION_PATTERN);
	}
	
	protected String getHostname() throws IOException {
		return executeAndFind(HOSTNAME_COMMAND, HOSTNAME_PATTERN);
	}
	
	protected DeviceSystem getSystem() throws IOException {
		return DeviceSystem.CISCO;
	}
	
	protected String getSystemString() throws IOException {
		return executeAndFind(SYSTEM_COMMAND, SYSTEM_PATTERN);
	}
	
	protected long getUptime() throws IOException {
		String uptime = executeAndFind(UPTIME_COMMAND, UPTIME_PATTERN);
		return Long.parseLong(uptime);
	}
	
	protected Set<Capability> getCapabilities() throws IOException {
		// TODO: implement
		return new HashSet<Capability>();
	}
	
	@Override
	protected Capability getMajorCapability() throws IOException {
		// TODO: implement
		return null;
	}
	
	protected Set<NetworkInterface> getInterfaces() throws IOException {
		// TODO: implement
		return null;
	}
	
	protected Set<IPAddress> getManagementAddresses() throws IOException {
		// TODO: implement
		return null;
	}
	
	protected boolean setHostname(String hostname) throws IOException {
		// TODO: implement
		return false;
	}
	
	@Override
	public Map<IPv4Address, NetworkInterface> discoverNeighbors() {
		// TODO: implement
		return null;
	}
	
}