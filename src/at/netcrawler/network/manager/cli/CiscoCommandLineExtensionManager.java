package at.netcrawler.network.manager.cli;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.network.CDPNeighbors;
import at.netcrawler.network.CDPNeighbors.Neighbor;
import at.netcrawler.network.Capability;
import at.netcrawler.network.manager.CiscoExtensionManager;


public class CiscoCommandLineExtensionManager extends
		CiscoExtensionManager<CiscoCommandLineDeviceManager> {
	
	private static final String MODEL_NUMBER_COMMAND = "show version";
	private static final Pattern MODEL_NUMBER_PATTERN = Pattern.compile(
			"^model number\\s*: (.*)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE);
	private static final int MODEL_NUMBER_GROUP = 1;
	
	private static final String SYSTEM_SERIAL_NUMBER_COMMAND = "show version";
	private static final Pattern SYSTEM_SERIAL_NUMBER_PATTERN = Pattern.compile(
			"^System serial number\\s*: (.*)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE);
	private static final int SYSTEM_SERIAL_NUMBER_GROUP = 1;
	
	private static final String PROCESSOR_STRING_COMMAND = "show version";
	private static final Pattern PROCESSOR_STRING_PATTERN = Pattern.compile(
			"^(.*?) with (.+?) bytes of memory.*", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE);
	private static final int PROCESSOR_STRING_GROUP = 0;
	
	private static final String CDP_NEIGHBORS_COMMAND = "show cdp neighbors detail";
	private static final Pattern CDP_NEIGHBORS_SEPARATOR = Pattern.compile(
			"^-{2,}$", Pattern.MULTILINE);
	private static final Pattern CDP_NEIGHBORS_NAME_PATTERN = Pattern.compile(
			"^device id\\s*: (.*?)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE);
	private static final int CDP_NEIGHBORS_NAME_GROUP = 1;
	private static final Pattern CDP_NEIGHBORS_LOCAL_INTERFACE_PATTERN = Pattern.compile(
			".*port id \\(outgoing port\\): (.*?)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE);
	private static final int CDP_NEIGHBORS_LOCAL_INTERFACE_GROUP = 1;
	private static final Pattern CDP_NEIGHBORS_HOLD_TIME_PATTERN = Pattern.compile(
			"^holdtime\\s*: (.*?)( (.*))?$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE);
	private static final int CDP_NEIGHBORS_HOLD_TIME_GROUP = 1;
	private static final Pattern CDP_NEIGHBORS_CAPABILITIES_PATTERN = Pattern.compile(
			".*capabilities\\s*: (.*?)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE);
	private static final int CDP_NEIGHBORS_CAPABILITIES_GROUP = 1;
	private static final Pattern CDP_NEIGHBORS_CAPABILITIES_SEPARATOR = Pattern.compile(" ");
	private static final Map<String, Capability> CDP_NEIGHBORS_CAPABILITIES_MAP = new HashMap<String, Capability>() {
		private static final long serialVersionUID = -2377198487555883189L;
		
		{
			// TODO: complete it
			put("router", Capability.ROUTER);
			put("switch", Capability.SWITCH);
		}
	};
	private static final Pattern CDP_NEIGHBORS_PLATFORM_PATTERN = Pattern.compile(
			"^platform\\s*: (.*?),.*", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE);
	private static final int CDP_NEIGHBORS_PLATFORM_GROUP = 1;
	private static final Pattern CDP_NEIGHBORS_REMOTE_INTERFACE_PATTERN = Pattern.compile(
			"^interface\\s*: (.*?),.*", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE);
	private static final int CDP_NEIGHBORS_REMOTE_INTERFACE_GROUP = 1;
	private static final Pattern CDP_NEIGHBORS_MANAGEMENT_ADDRESS_PATTERN = Pattern.compile(
			".*ip address\\s*: (.*?)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE);
	private static final int CDP_NEIGHBORS_MANAGEMENT_ADDRESS_GROUP = 1;
	
	private static String find(String string, Pattern pattern, int group) {
		Matcher matcher = pattern.matcher(string);
		if (!matcher.find()) return null;
		return matcher.group(group);
	}
	
	private static List<String> findAll(String string, Pattern pattern,
			int group) {
		List<String> result = new LinkedList<String>();
		
		int offset = 0;
		Matcher matcher = pattern.matcher(string);
		while (true) {
			if (!matcher.find(offset)) break;
			result.add(matcher.group(group));
			offset = matcher.end();
		}
		
		return result;
	}
	
	@Override
	public String getModelNumber() throws IOException {
		return deviceManager.executeAndFind(MODEL_NUMBER_COMMAND,
				MODEL_NUMBER_PATTERN, MODEL_NUMBER_GROUP);
	}
	
	@Override
	public String getSystemSerialNumber() throws IOException {
		return deviceManager.executeAndFind(SYSTEM_SERIAL_NUMBER_COMMAND,
				SYSTEM_SERIAL_NUMBER_PATTERN, SYSTEM_SERIAL_NUMBER_GROUP);
	}
	
	@Override
	public String getProcessorString() throws IOException {
		return deviceManager.executeAndFind(PROCESSOR_STRING_COMMAND,
				PROCESSOR_STRING_PATTERN, PROCESSOR_STRING_GROUP);
	}
	
	@Override
	public CDPNeighbors getCDPNeighbors() throws IOException {
		CDPNeighbors result = new CDPNeighbors();
		
		String output = deviceManager.execute(CDP_NEIGHBORS_COMMAND);
		String[] neighborStrings = CDP_NEIGHBORS_SEPARATOR.split(output);
		
		for (String neighborString : neighborStrings) {
			neighborString = neighborString.trim();
			
			if (neighborString.isEmpty()) continue;
			
			String name = find(neighborString, CDP_NEIGHBORS_NAME_PATTERN,
					CDP_NEIGHBORS_NAME_GROUP);
			
			String localInterface = find(neighborString,
					CDP_NEIGHBORS_LOCAL_INTERFACE_PATTERN,
					CDP_NEIGHBORS_LOCAL_INTERFACE_GROUP);
			
			int holdTime = Integer.parseInt(find(neighborString,
					CDP_NEIGHBORS_HOLD_TIME_PATTERN,
					CDP_NEIGHBORS_HOLD_TIME_GROUP));
			
			Set<Capability> capabilities = new HashSet<Capability>();
			String capabilitiesString = find(neighborString,
					CDP_NEIGHBORS_CAPABILITIES_PATTERN,
					CDP_NEIGHBORS_CAPABILITIES_GROUP);
			String[] capabilityStrings = CDP_NEIGHBORS_CAPABILITIES_SEPARATOR.split(capabilitiesString);
			for (String capabilityString : capabilityStrings) {
				capabilityString = capabilityString.toLowerCase();
				Capability capability = CDP_NEIGHBORS_CAPABILITIES_MAP.get(capabilityString);
				if (capability == null) continue;
				capabilities.add(capability);
			}
			
			String platform = find(neighborString,
					CDP_NEIGHBORS_PLATFORM_PATTERN,
					CDP_NEIGHBORS_PLATFORM_GROUP);
			
			String remoteInterface = find(neighborString,
					CDP_NEIGHBORS_REMOTE_INTERFACE_PATTERN,
					CDP_NEIGHBORS_REMOTE_INTERFACE_GROUP);
			
			Set<IPv4Address> managementAddresses = new HashSet<IPv4Address>();
			List<String> managementAddressStrings = findAll(neighborString,
					CDP_NEIGHBORS_MANAGEMENT_ADDRESS_PATTERN,
					CDP_NEIGHBORS_MANAGEMENT_ADDRESS_GROUP);
			for (String managementAddressString : managementAddressStrings) {
				IPv4Address managementAddress = IPv4Address.getByAddress(managementAddressString);
				managementAddresses.add(managementAddress);
			}
			
			Neighbor newNeighbor = new Neighbor(name, localInterface, holdTime,
					capabilities, platform, remoteInterface,
					managementAddresses);
			result.addNeighbor(newNeighbor);
		}
		
		return result;
	}
	
	@Override
	public boolean hasExtension() throws IOException {
		return true;
	}
	
}