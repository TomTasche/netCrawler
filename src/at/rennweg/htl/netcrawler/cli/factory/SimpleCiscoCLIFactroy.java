package at.rennweg.htl.netcrawler.cli.factory;

import java.net.InetAddress;

import at.andiwand.library.util.cli.CommandLine;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;


public interface SimpleCiscoCLIFactroy {
	
	public CommandLine getCommandLine(InetAddress address, SimpleCiscoUser user) throws Exception;
	
}