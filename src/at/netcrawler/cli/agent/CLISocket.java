package at.netcrawler.cli.agent;

import java.io.Reader;
import java.io.Writer;


public class CLISocket {
	
	private final Reader reader;
	private final Writer writer;
	
	public CLISocket(Reader reader, Writer writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	public Reader getReader() {
		return reader;
	}
	
	public Writer getWriter() {
		return writer;
	}
	
}