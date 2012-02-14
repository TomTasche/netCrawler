package at.netcrawler.network.manager.cli;

import java.io.IOException;
import java.util.regex.Pattern;

import at.andiwand.library.util.QuickPattern;
import at.netcrawler.network.manager.CiscoRouterExtensionManager;
import at.netcrawler.network.manager.DeviceManager;


public class CiscoCLIRouterExtensionManager extends CiscoRouterExtensionManager {
	
	private static final String PROCESSOR_BOARD_ID_COMMAND = "show version";
	private static final QuickPattern PROCESSOR_BOARD_ID_PATTERN = new QuickPattern(
			"^(.*?) with (.+?) bytes of memory.*", Pattern.MULTILINE
					| Pattern.CASE_INSENSITIVE, 0);
	
	private CiscoCLIDeviceManager deviceManager;
	
	@Override
	protected String getProcessorBoardId() throws IOException {
		return deviceManager.executeAndFind(PROCESSOR_BOARD_ID_COMMAND,
				PROCESSOR_BOARD_ID_PATTERN);
	}
	
	@Override
	protected void setDeviceManager(DeviceManager deviceManager) {
		super.setDeviceManager(deviceManager);
		
		this.deviceManager = (CiscoCLIDeviceManager) deviceManager;
	}
	
	// TODO: implement
	@Override
	public boolean hasExtension() throws IOException {
		return false;
	}
	
}