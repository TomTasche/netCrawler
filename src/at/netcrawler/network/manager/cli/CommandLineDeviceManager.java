package at.netcrawler.network.manager.cli;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.netcrawler.cli.agent.PromtPatternCLIAgent;
import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.NetworkDevice;


public abstract class CommandLineDeviceManager extends DeviceManager {
	
	protected final PromtPatternCLIAgent agent;
	
	private boolean commandOutputCaching;
	private Map<String, String> commandOutputCache = new HashMap<String, String>();
	
	public CommandLineDeviceManager(NetworkDevice device,
			PromtPatternCLIAgent agent) {
		super(device);
		
		this.agent = agent;
	}
	
	public String execute(String command) throws IOException {
		if (commandOutputCaching) {
			String commandOutput = commandOutputCache.get(command);
			
			if (commandOutput == null) {
				commandOutput = agent.execute(command);
				commandOutputCache.put(command, commandOutput);
			}
			
			return commandOutput;
		}
		
		return agent.execute(command);
	}
	
	public String executeAndFind(String command, Pattern pattern, int group)
			throws IOException {
		String result = execute(command);
		Matcher matcher = pattern.matcher(result);
		if (!matcher.find()) return null;
		return matcher.group(group);
	}
	
	private void setCommandOutputCaching(boolean commandOutputCaching) {
		if (this.commandOutputCaching == commandOutputCaching) return;
		
		this.commandOutputCaching = commandOutputCaching;
		
		if (!commandOutputCaching) commandOutputCache.clear();
	}
	
	@Override
	public void readDevice() throws IOException {
		try {
			setCommandOutputCaching(true);
			
			super.readDevice();
		} finally {
			setCommandOutputCaching(false);
		}
	}
	
}