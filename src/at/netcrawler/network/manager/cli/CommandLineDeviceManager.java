package at.netcrawler.network.manager.cli;

import at.netcrawler.cli.agent.CommandLineAgent;
import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.NetworkDevice;


public abstract class CommandLineDeviceManager extends DeviceManager {
	
	protected final CommandLineAgent agent;
	
	public CommandLineDeviceManager(NetworkDevice device, CommandLineAgent agent) {
		super(device);
		
		this.agent = agent;
	}
	
}