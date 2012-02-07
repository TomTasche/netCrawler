package at.netcrawler.network.manager.cli;

import java.io.IOException;

import at.andiwand.library.util.QuickPattern;
import at.netcrawler.cli.agent.PromtPatternCLIAgent;
import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.NetworkDevice;


public abstract class CLIDeviceManager extends DeviceManager {
	
	protected final PromtPatternCLIAgent agent;
	
	public CLIDeviceManager(NetworkDevice device, PromtPatternCLIAgent agent) {
		super(device);
		
		this.agent = agent;
	}
	
	public String execute(String command) throws IOException {
		return agent.execute(command);
	}
	
	public String executeAndFind(String command, QuickPattern pattern)
			throws IOException {
		String result = execute(command);
		return pattern.find(result);
	}
	
}