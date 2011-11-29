package at.netcrawler.network.connection.ssh.executor;

import java.io.IOException;

import at.netcrawler.network.connection.ssh.SSHClient;


public interface SSHExecutor extends SSHClient {
	
	public String execute(String command) throws IOException;
	
	public int getLastExitStatus();
	
	public void close() throws IOException;
	
}