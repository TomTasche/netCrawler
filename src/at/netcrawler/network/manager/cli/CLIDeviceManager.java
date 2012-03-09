package at.netcrawler.network.manager.cli;

import java.io.IOException;

import at.andiwand.library.util.QuickPattern;
import at.netcrawler.cli.agent.PromtCommandLineAgent;
import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.NetworkDevice;


public abstract class CLIDeviceManager extends DeviceManager {
	
	protected final PromtCommandLineAgent agent;
	
	public CLIDeviceManager(NetworkDevice device, PromtCommandLineAgent agent) {
		super(device);
		
		this.agent = agent;
	}
	
	protected final String execute(String command) throws IOException {
		return agent.execute(command);
	}
	
	protected final String executeAndFind(String command, QuickPattern pattern)
			throws IOException {
		String result = execute(command);
		return pattern.findGroup(result);
	}
	
}