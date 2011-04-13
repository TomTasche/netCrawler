package at.rennweg.htl.netcrawler.cli.executor.factory;

import java.net.InetAddress;

import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;
import at.rennweg.htl.netcrawler.cli.executor.SimpleCachedRemoteExecutor;
import at.rennweg.htl.netcrawler.cli.executor.SimpleRemoteExecutor;
import at.rennweg.htl.netcrawler.network.ssh.SSH1Executor;
import at.rennweg.htl.netcrawler.network.ssh.SSH2Executor;


public class SimpleCiscoSSHExecutorFactory implements SimpleCiscoRemoteExecutorFactory {
	
	@Override
	public SimpleRemoteExecutor getRemoteExecutor(InetAddress address, SimpleCiscoUser user) throws Exception {
		SimpleRemoteExecutor result;
		
		try {
			result = new SSH2Executor(address, user.getUsername(), user.getPassword());
		} catch (Exception e) {
			result = new SSH1Executor(address, user.getUsername(), user.getPassword());
		}
		
		SimpleCachedRemoteExecutor cachedRemoteExecutor = new SimpleCachedRemoteExecutor(result);
		
		return cachedRemoteExecutor;
	}
	
}