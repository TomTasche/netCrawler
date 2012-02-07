package at.netcrawler.network.manager.cli;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.util.QuickPattern;
import at.netcrawler.network.CDPNeighbors;
import at.netcrawler.network.CDPNeighbors.Neighbor;
import at.netcrawler.network.Capability;
import at.netcrawler.network.manager.CiscoExtensionManager;
import at.netcrawler.network.manager.DeviceManager;


public class CiscoCLIExtensionManager extends CiscoExtensionManager {
	
	private static final String MODEL_NUMBER_COMMAND = "show version";
	private static final QuickPattern MODEL_NUMBER_PATTERN = new QuickPattern(
			"^model number\\s*: (.*)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 1);
	
	private static final String SYSTEM_SERIAL_NUMBER_COMMAND = "show version";
	private static final QuickPattern SYSTEM_SERIAL_NUMBER_PATTERN = new QuickPattern(
			"^System serial number\\s*: (.*)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 1);
	
	private static final String PROCESSOR_STRING_COMMAND = "show version";
	private static final QuickPattern PROCESSOR_STRING_PATTERN = new QuickPattern(
			"^(.*?) with (.+?) bytes of memory.*", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 0);
	
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
	private static final Pattern CDP_NEIGHBORS_CAPABILITIES_SEPARATOR = Pattern.compile(" ");
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
	
	private CiscoCLIDeviceManager deviceManager;
	
	@Override
	public String getModelNumber() throws IOException {
		return deviceManager.executeAndFind(MODEL_NUMBER_COMMAND,
				MODEL_NUMBER_PATTERN);
	}
	
	@Override
	public String getSystemSerialNumber() throws IOException {
		return deviceManager.executeAndFind(SYSTEM_SERIAL_NUMBER_COMMAND,
				SYSTEM_SERIAL_NUMBER_PATTERN);
	}
	
	@Override
	public String getProcessorString() throws IOException {
		return deviceManager.executeAndFind(PROCESSOR_STRING_COMMAND,
				PROCESSOR_STRING_PATTERN);
	}
	
	@Override
	public CDPNeighbors getCDPNeighbors() throws IOException {
		CDPNeighbors result = new CDPNeighbors();
		
		String output = deviceManager.execute(CDP_NEIGHBORS_COMMAND);
		String[] neighborStrings = CDP_NEIGHBORS_SEPARATOR.split(output);
		
		for (String neighborString : neighborStrings) {
			neighborString = neighborString.trim();
			
			if (neighborString.isEmpty()) continue;
			
			String name = CDP_NEIGHBORS_NAME_PATTERN.find(neighborString);
			
			String localInterface = CDP_NEIGHBORS_LOCAL_INTERFACE_PATTERN.find(neighborString);
			
			int holdTime = Integer.parseInt(CDP_NEIGHBORS_HOLD_TIME_PATTERN.find(neighborString));
			
			Set<Capability> capabilities = new HashSet<Capability>();
			String capabilitiesString = CDP_NEIGHBORS_CAPABILITIES_PATTERN.find(neighborString);
			String[] capabilityStrings = CDP_NEIGHBORS_CAPABILITIES_SEPARATOR.split(capabilitiesString);
			for (String capabilityString : capabilityStrings) {
				capabilityString = capabilityString.toLowerCase();
				Capability capability = CDP_NEIGHBORS_CAPABILITIES_MAP.get(capabilityString);
				if (capability == null) continue;
				capabilities.add(capability);
			}
			
			String platform = CDP_NEIGHBORS_PLATFORM_PATTERN.find(neighborString);
			
			String remoteInterface = CDP_NEIGHBORS_REMOTE_INTERFACE_PATTERN.find(neighborString);
			
			Set<IPv4Address> managementAddresses = new HashSet<IPv4Address>();
			List<String> managementAddressStrings = CDP_NEIGHBORS_MANAGEMENT_ADDRESS_PATTERN.findAll(neighborString);
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
	protected void setDeviceManager(DeviceManager deviceManager) {
		super.setDeviceManager(deviceManager);
		
		this.deviceManager = (CiscoCLIDeviceManager) deviceManager;
	}
	
	@Override
	public boolean hasExtension() throws IOException {
		return true;
	}
	
}