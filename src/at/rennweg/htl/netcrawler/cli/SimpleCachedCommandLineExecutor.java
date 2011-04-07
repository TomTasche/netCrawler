package at.rennweg.htl.netcrawler.cli;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SimpleCachedCommandLineExecutor extends SimpleCommandLineExecutor {
	
	private final SimpleCommandLineExecutor executor;
	
	private final Map<String, String> commandCache =
		new HashMap<String, String>();
	
	
	public SimpleCachedCommandLineExecutor(SimpleCommandLineExecutor executor) {
		super(executor.commandLine);
		
		this.executor = executor;
	}
	
	
	@Override
	public String execute(String command) throws IOException {
		if (commandCache.containsKey(command)) return commandCache.get(command);
		
		String result = executor.execute(command);
		commandCache.put(command, result);
		
		return result;
	}
	
	public void clear() {
		commandCache.clear();
	}
	
}