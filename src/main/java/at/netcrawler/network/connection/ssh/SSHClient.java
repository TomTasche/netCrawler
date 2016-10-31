package at.netcrawler.network.connection.ssh;

import at.andiwand.library.cli.CommandLineInterface;


public interface SSHClient extends CommandLineInterface {
	
	public SSHVersion getVersion();
	
}