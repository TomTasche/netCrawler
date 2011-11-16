package at.netcrawler.network.connection.ssh;

import at.andiwand.library.cli.CommandLine;


public interface SSHClient extends CommandLine {
	
	public SSHVersion getVersion();
	
}