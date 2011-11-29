package at.netcrawler.network.connection.ssh.executor.impl;

import java.io.IOException;

import at.netcrawler.network.accessor.IPDeviceAccessor;
import at.netcrawler.network.connection.ssh.executor.LocalSSHExecutorConnection;
import at.netcrawler.network.connection.ssh.executor.SSHExecutorConnectionSettings;


public abstract class LocalSSHExecutorConnectionImpl extends
		LocalSSHExecutorConnection {
	
	public static LocalSSHExecutorConnectionImpl getInstance(
			IPDeviceAccessor accessor, SSHExecutorConnectionSettings settings)
			throws IOException {
		switch (settings.getVersion()) {
		case VERSION1:
			return new LocalSSH1ExecutorConnectionImpl(accessor, settings);
		case VERSION2:
			return new LocalSSH2ExecutorConnectionImpl(accessor, settings);
		}
		
		throw new IllegalStateException("Unreachable section");
	}
	
	public LocalSSHExecutorConnectionImpl(IPDeviceAccessor accessor,
			SSHExecutorConnectionSettings settings) throws IOException {
		super(accessor, settings);
	}
	
	@Override
	public abstract String execute(String command) throws IOException;
	
	@Override
	public abstract int getLastExitStatus();
	
	@Override
	protected abstract void closeImpl() throws IOException;
	
}