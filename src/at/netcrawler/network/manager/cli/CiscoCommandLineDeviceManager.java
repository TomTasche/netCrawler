package at.netcrawler.network.manager.cli;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import at.andiwand.library.network.ip.IPAddress;
import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.util.QuickPattern;
import at.netcrawler.cli.agent.PromptCommandLineAgent;
import at.netcrawler.network.CDPNeighbor;
import at.netcrawler.network.Capability;
import at.netcrawler.network.model.DeviceSystem;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.model.extension.CiscoDeviceExtension;


public class CiscoCommandLineDeviceManager extends CommandLineDeviceManager {
	
	private static final String IDENTICATION_COMMAND = "show version";
	private static final QuickPattern IDENTICATION_PATTERN = new QuickPattern(
			"^processor board id ((.*?)( \\((.*?)\\))?)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 2);
	
	private static final String HOSTNAME_COMMAND = "show running-config";
	private static final QuickPattern HOSTNAME_PATTERN = new QuickPattern(
			"^hostname (.*?)$", Pattern.MULTILINE, 1);
	
	private static final String SYSTEM_COMMAND = "show version";
	private static final QuickPattern SYSTEM_PATTERN = new QuickPattern(
			".*? (C?\\d+) software \\((.+?)\\).*", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 0);
	
	// TODO: implement
	// private static final String UPTIME_COMMAND = "show version";
	// private static final QuickPattern UPTIME_PATTERN = new QuickPattern(
	// ".*uptime is (.*)", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE, 1);
	
	private static final String SOFTWARE_COMMAND = "show version";
	private static final QuickPattern SOFTWARE_PATTERN = new QuickPattern(
			".*? (C?\\d+) software \\((.+?)\\).*", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 1);
	
	// private static final String INTERFACES_COMMAND = "show ip interfaces";
	
	private static final String MANAGEMENT_ADDRESSES_COMMAND = "show ip interface brief";
	private static final Pattern MANAGEMENT_ADDRESSES_SEPARATOR = Pattern
			.compile("\n", Pattern.MULTILINE);
	private static final QuickPattern MANAGEMENT_ADDRESSES_PATTERN = new QuickPattern(
			"((\\d{0,3}\\.){3}\\d{0,3})", 0);
	
	public CiscoCommandLineDeviceManager(NetworkDevice device,
			PromptCommandLineAgent agent) {
		super(device, agent);
		
		addExtensionManager(new CiscoDeviceCommandLineExtensionManager());
		addExtensionManager(new CiscoSwitchCommandLineExtensionManager());
		addExtensionManager(new CiscoRouterCommandLineExtensionManager());
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
		// TODO: implement
		return 0;
	}
	
	protected Set<Capability> getCapabilities() throws IOException {
		// TODO: implement
		return new HashSet<Capability>();
	}
	
	@Override
	protected Capability getMajorCapability() throws IOException {
		String software = executeAndFind(SOFTWARE_COMMAND, SOFTWARE_PATTERN);
		software = software.toUpperCase();
		
		if (software.startsWith("28")) {
			return Capability.ROUTER;
		} else if (software.startsWith("C35")) {
			return Capability.SWITCH;
		}
		
		return null;
	}
	
	protected Set<NetworkInterface> getInterfaces() throws IOException {
		// TODO: implement
		return null;
	}
	
	protected Set<IPAddress> getManagementAddresses() throws IOException {
		Set<IPAddress> result = new HashSet<IPAddress>();
		
		String output = execute(MANAGEMENT_ADDRESSES_COMMAND);
		String[] interfaces = MANAGEMENT_ADDRESSES_SEPARATOR.split(output);
		
		for (String interfaze : interfaces) {
			String addressString = MANAGEMENT_ADDRESSES_PATTERN
					.findGroup(interfaze);
			if (addressString == null) continue;
			IPv4Address address = new IPv4Address(addressString);
			result.add(address);
		}
		
		return result;
	}
	
	// TODO: improve
	protected boolean setHostname(String hostname) throws IOException {
		execute("configure terminal");
		execute("hostname " + hostname);
		execute("end");
		return hostname.equals(getHostname());
	}
	
	// TODO: implement
	@Override
	@SuppressWarnings("unchecked")
	public Set<IPv4Address> discoverNeighbors() {
		Set<IPv4Address> result = new HashSet<IPv4Address>();
		List<CDPNeighbor> neighbors = (List<CDPNeighbor>) device
				.getValue(CiscoDeviceExtension.CDP_NEIGHBORS);
		
		for (CDPNeighbor neighbor : neighbors) {
			Set<IPv4Address> managementAddresses = neighbor
					.getManagementAddresses();
			if (managementAddresses.isEmpty()) continue;
			
			result.add(managementAddresses.iterator().next());
		}
		
		return result;
	}
	
}