package at.netcrawler.network.manager.cli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.util.QuickPattern;
import at.netcrawler.network.manager.CiscoDeviceExtensionManager;
import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.CDPNeighbor;
import at.netcrawler.network.model.Capability;


public class CiscoDeviceCommandLineExtensionManager extends
		CiscoDeviceExtensionManager {
	
	private static final String CDP_NEIGHBORS_COMMAND = "show cdp neighbors detail";
	private static final Pattern CDP_NEIGHBORS_SEPARATOR = Pattern.compile(
			"^-{2,}$", Pattern.MULTILINE);
	private static final QuickPattern CDP_NEIGHBORS_NAME_PATTERN = new QuickPattern(
			"^device id\\s*: (.*?)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 1);
	private static final QuickPattern CDP_NEIGHBORS_LOCAL_INTERFACE_PATTERN = new QuickPattern(
			".*port id \\(outgoing port\\): (.*?)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 1);
	private static final QuickPattern CDP_NEIGHBORS_HOLD_TIME_PATTERN = new QuickPattern(
			"^holdtime\\s*: (.*?)( (.*))?$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 1);
	private static final QuickPattern CDP_NEIGHBORS_CAPABILITIES_PATTERN = new QuickPattern(
			".*capabilities\\s*: (.*?)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 1);
	private static final Pattern CDP_NEIGHBORS_CAPABILITIES_SEPARATOR = Pattern
			.compile(" ");
	private static final Map<String, Capability> CDP_NEIGHBORS_CAPABILITIES_MAP = new HashMap<String, Capability>() {
		private static final long serialVersionUID = -2377198487555883189L;
		
		{
			// TODO: complete it
			put("router", Capability.ROUTER);
			put("switch", Capability.SWITCH);
		}
	};
	private static final QuickPattern CDP_NEIGHBORS_PLATFORM_PATTERN = new QuickPattern(
			"^platform\\s*: (.*?),.*", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 1);
	private static final QuickPattern CDP_NEIGHBORS_REMOTE_INTERFACE_PATTERN = new QuickPattern(
			"^interface\\s*: (.*?),.*", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 1);
	private static final QuickPattern CDP_NEIGHBORS_MANAGEMENT_ADDRESS_PATTERN = new QuickPattern(
			".*ip address\\s*: (.*?)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 1);
	
	private CiscoCommandLineDeviceManager deviceManager;
	
	@Override
	protected List<CDPNeighbor> getCDPNeighbors() throws IOException {
		List<CDPNeighbor> result = new ArrayList<CDPNeighbor>();
		
		String output = deviceManager.execute(CDP_NEIGHBORS_COMMAND);
		String[] neighborStrings = CDP_NEIGHBORS_SEPARATOR.split(output);
		
		for (String neighborString : neighborStrings) {
			neighborString = neighborString.trim();
			
			if (neighborString.isEmpty()) continue;
			
			String name = CDP_NEIGHBORS_NAME_PATTERN.findGroup(neighborString);
			
			String localInterface = CDP_NEIGHBORS_LOCAL_INTERFACE_PATTERN
					.findGroup(neighborString);
			
			int holdTime = Integer.parseInt(CDP_NEIGHBORS_HOLD_TIME_PATTERN
					.findGroup(neighborString));
			
			Set<Capability> capabilities = new HashSet<Capability>();
			String capabilitiesString = CDP_NEIGHBORS_CAPABILITIES_PATTERN
					.findGroup(neighborString);
			String[] capabilityStrings = CDP_NEIGHBORS_CAPABILITIES_SEPARATOR
					.split(capabilitiesString);
			for (String capabilityString : capabilityStrings) {
				capabilityString = capabilityString.toLowerCase();
				Capability capability = CDP_NEIGHBORS_CAPABILITIES_MAP
						.get(capabilityString);
				if (capability == null) continue;
				capabilities.add(capability);
			}
			
			String platform = CDP_NEIGHBORS_PLATFORM_PATTERN
					.findGroup(neighborString);
			
			String remoteInterface = CDP_NEIGHBORS_REMOTE_INTERFACE_PATTERN
					.findGroup(neighborString);
			
			Set<IPv4Address> managementAddresses = new HashSet<IPv4Address>();
			List<String> managementAddressStrings = CDP_NEIGHBORS_MANAGEMENT_ADDRESS_PATTERN
					.findGroupAll(neighborString);
			for (String managementAddressString : managementAddressStrings) {
				IPv4Address managementAddress = new IPv4Address(
						managementAddressString);
				managementAddresses.add(managementAddress);
			}
			
			CDPNeighbor neighbor = new CDPNeighbor();
			neighbor.setHostname(name);
			neighbor.setLocalInterface(localInterface);
			neighbor.setHoldTime(holdTime);
			neighbor.setCapabilities(capabilities);
			neighbor.setSystemDescription(platform);
			neighbor.setRemoteInterface(remoteInterface);
			neighbor.setManagementAddresses(managementAddresses);
			result.add(neighbor);
		}
		
		return result;
	}
	
	@Override
	protected void setDeviceManager(DeviceManager deviceManager) {
		super.setDeviceManager(deviceManager);
		this.deviceManager = (CiscoCommandLineDeviceManager) deviceManager;
	}
	
	@Override
	public boolean hasExtension() throws IOException {
		return true;
	}
	
}