package at.rennweg.htl.netcrawler.cli.executor;

import java.io.IOException;


public interface SimpleRemoteExecutor {
	
	public String execute(String command) throws IOException;
	
	public void close() throws IOException;
	
}