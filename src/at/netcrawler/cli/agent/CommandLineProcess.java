package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;

import at.andiwand.library.io.CloseableReader;
import at.andiwand.library.io.CloseableWriter;


public abstract class CommandLineProcess {
	
	private final String command;
	
	private CloseableReader inCloser;
	private CloseableWriter outCloser;
	protected PushbackReader in;
	protected Writer out;
	
	private final Object monitor = new Object();
	private boolean closed;
	
	public CommandLineProcess(String command) {
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}
	
	public final void execute(Reader in, Writer out) {
		this.in = initReader(in);
		this.out = initWriter(out);
		
		executeImpl();
	}
	
	protected void executeImpl() {}
	
	private PushbackReader initReader(Reader reader) {
		inCloser = new CloseableReader(reader);
		reader = hookReader(reader);
		return new PushbackReader(reader);
	}
	
	private Writer initWriter(Writer writer) {
		outCloser = new CloseableWriter(writer);
		writer = hookWriter(writer);
		return writer;
	}
	
	protected Reader hookReader(Reader reader) {
		return reader;
	}
	
	protected Writer hookWriter(Writer writer) {
		return writer;
	}
	
	@Override
	public String toString() {
		return command;
	}
	
	protected final void closeStreams() {
		inCloser.close();
		outCloser.close();
		
		synchronized (monitor) {
			closed = true;
			monitor.notifyAll();
		}
	}
	
	public final void close() throws IOException {
		closeImpl();
		closeStreams();
	}
	
	protected void closeImpl() {}
	
	public void waitFor() throws InterruptedException {
		synchronized (monitor) {
			if (closed) return;
			
			monitor.wait();
		}
	}
	
}