package at.rennweg.htl.netcrawler.cli.executor.factory;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

import at.andiwand.library.util.cli.CommandLine;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoCommandLineExecutor;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;
import at.rennweg.htl.netcrawler.cli.executor.SimpleCachedRemoteExecutor;
import at.rennweg.htl.netcrawler.cli.executor.SimpleRemoteExecutor;
import at.rennweg.htl.netcrawler.cli.factory.SimpleCiscoCLIFactroy;
import at.rennweg.htl.netcrawler.cli.factory.SimplePacketTracerTelnetFactory;


public class SimplePacketTracerTelnetExecutorFactory implements SimpleCiscoRemoteExecutorFactory {
	
	private SimpleCiscoCLIFactroy cliFactroy;
	
	public SimplePacketTracerTelnetExecutorFactory(Inet4Address deviceAddress, Inet4Address deviceGateway) throws IOException, InterruptedException {
		cliFactroy = new SimplePacketTracerTelnetFactory(deviceAddress, deviceGateway);
	}
	public SimplePacketTracerTelnetExecutorFactory(InetAddress address, int port, String networkName, Inet4Address deviceAddress, Inet4Address deviceGateway) throws IOException, InterruptedException {
		cliFactroy = new SimplePacketTracerTelnetFactory(address, port, networkName, SimplePacketTracerTelnetFactory.DEFAULT_MAC_ADDRESS, deviceAddress, deviceGateway);
	}
	
	@Override
	public SimpleRemoteExecutor getRemoteExecutor(InetAddress address, SimpleCiscoUser user) throws Exception {
		CommandLine ciscoCli = cliFactroy.getCommandLine(address, user);
		SimpleCiscoCommandLineExecutor executor = new SimpleCiscoCommandLineExecutor(ciscoCli);
		SimpleCachedRemoteExecutor cachedRemoteExecutor = new SimpleCachedRemoteExecutor(executor);
		
		return cachedRemoteExecutor;
	}
	
}