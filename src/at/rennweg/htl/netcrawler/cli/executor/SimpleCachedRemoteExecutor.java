package at.rennweg.htl.netcrawler.cli.executor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SimpleCachedRemoteExecutor implements SimpleRemoteExecutor {
	
	private final SimpleRemoteExecutor executor;
	
	private final Map<String, String> commandCache =
		new HashMap<String, String>();
	
	
	public SimpleCachedRemoteExecutor(SimpleRemoteExecutor executor) {
		this.executor = executor;
	}
	
	
	@Override
	public String execute(String command) throws IOException {
		if (commandCache.containsKey(command)) return commandCache.get(command);
		
		String result = executor.execute(command);
		commandCache.put(command, result);
		
		return result;
	}
	
	public void clearAll() {
		commandCache.clear();
	}
	
	@Override
	public void close() throws IOException {
		executor.close();
	}
	
}