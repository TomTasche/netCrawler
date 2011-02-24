package at.rennweg.htl.netcrawler.network.crawler;

import java.net.InetAddress;

import at.andiwand.library.util.cli.CommandLine;


public interface SimpleCLIFactroy {
	
	public CommandLine getCommandLine(InetAddress address);
	
}