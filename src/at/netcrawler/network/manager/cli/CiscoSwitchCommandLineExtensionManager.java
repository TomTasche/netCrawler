package at.netcrawler.network.manager.cli;

import java.io.IOException;
import java.util.regex.Pattern;

import at.andiwand.library.util.QuickPattern;
import at.netcrawler.network.manager.CiscoSwitchExtensionManager;
import at.netcrawler.network.manager.DeviceManager;


public class CiscoSwitchCommandLineExtensionManager extends
		CiscoSwitchExtensionManager {
	
	private static final String MODEL_NUMBER_COMMAND = "show version";
	private static final QuickPattern MODEL_NUMBER_PATTERN = new QuickPattern(
			"^model number\\s*: (.*)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 1);
	
	private static final String SYSTEM_SERIAL_NUMBER_COMMAND = "show version";
	private static final QuickPattern SYSTEM_SERIAL_NUMBER_PATTERN = new QuickPattern(
			"^System serial number\\s*: (.*)$", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 1);
	
	private CiscoCommandLineDeviceManager deviceManager;
	
	@Override
	protected String getModelNumber() throws IOException {
		return deviceManager.executeAndFind(MODEL_NUMBER_COMMAND,
				MODEL_NUMBER_PATTERN);
	}
	
	@Override
	protected String getSystemSerialNumber() throws IOException {
		return deviceManager.executeAndFind(SYSTEM_SERIAL_NUMBER_COMMAND,
				SYSTEM_SERIAL_NUMBER_PATTERN);
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