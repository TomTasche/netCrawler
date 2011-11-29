package at.netcrawler.network.connection.ssh.executor;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.executor.impl.LocalSSHExecutorConnectionImpl;


public class LocalSSHExecutorConnection extends SSHExecutorConnection {
	
	private LocalSSHExecutorConnectionImpl executor;
	
	public LocalSSHExecutorConnection(IPDeviceAccessor accessor,
			SSHExecutorConnectionSettings settings) throws IOException {
		super(accessor, settings);
		
		executor = LocalSSHExecutorConnectionImpl.getInstance(accessor,
				settings);
	}
	
	@Override
	public String execute(String command) throws IOException {
		return executor.execute(command);
	}
	
	@Override
	public int getLastExitStatus() {
		return executor.getLastExitStatus();
	}
	
	@Override
	protected void closeImpl() throws IOException {
		executor.close();
	}
	
}