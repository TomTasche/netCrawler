package at.rennweg.htl.netcrawler.cli.factory;

import java.net.InetAddress;

import at.andiwand.library.util.cli.CommandLine;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;
import at.rennweg.htl.netcrawler.network.ssh.SSH1Client;
import at.rennweg.htl.netcrawler.network.ssh.SSH2Client;


public class SimpleSSHFactory implements SimpleCLIFactroy {
	
	@Override
	public CommandLine getCommandLine(InetAddress address, SimpleCiscoUser user) throws Exception {
		CommandLine result;
		
		try {
			result = new SSH2Client(address, user.getUsername(), user.getPassword());
		} catch (Exception e) {
			result = new SSH1Client(address, user.getUsername(), user.getPassword());
		}
		
		return result;
	}
	
}