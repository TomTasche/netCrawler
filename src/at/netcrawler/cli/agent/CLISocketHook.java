package at.netcrawler.cli.agent;

import java.io.Reader;
import java.io.Writer;


// TODO filter
public abstract class CLISocketHook {
	
	public CLISocket hookSocket(CLISocket socket) {
		Reader reader = hookReader(socket.getReader());
		Writer writer = hookWriter(socket.getWriter());
		
		return new CLISocket(reader, writer);
	}
	
	protected Reader hookReader(Reader reader) {
		return reader;
	}
	
	protected Writer hookWriter(Writer writer) {
		return writer;
	}
	
}