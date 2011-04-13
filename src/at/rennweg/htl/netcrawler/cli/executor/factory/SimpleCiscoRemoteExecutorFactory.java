package at.rennweg.htl.netcrawler.cli.executor.factory;

import java.net.InetAddress;

import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;
import at.rennweg.htl.netcrawler.cli.executor.SimpleRemoteExecutor;


public interface SimpleCiscoRemoteExecutorFactory {
	
	public SimpleRemoteExecutor getRemoteExecutor(InetAddress address, SimpleCiscoUser user) throws Exception;
	
}