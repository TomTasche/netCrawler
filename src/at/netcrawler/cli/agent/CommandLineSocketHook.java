package at.netcrawler.cli.agent;

import java.io.Reader;
import java.io.Writer;


public class CommandLineSocketHook {
	
	public CommandLineSocket hookSocket(CommandLineSocket socket) {
		Reader reader = hookReader(socket.getReader());
		Writer writer = hookWriter(socket.getWriter());
		
		return new CommandLineSocket(reader, writer);
	}
	
	protected Reader hookReader(Reader reader) {
		return reader;
	}
	
	protected Writer hookWriter(Writer writer) {
		return writer;
	}
	
}