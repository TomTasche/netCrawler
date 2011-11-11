package at.netcrawler.network.connection.telnet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.andiwand.library.cli.CommandLine;


public interface TelnetClient extends CommandLine {
	
	public InputStream getInputStream() throws IOException;
	public OutputStream getOutputStream() throws IOException;
	
	public void close() throws IOException;
	
}