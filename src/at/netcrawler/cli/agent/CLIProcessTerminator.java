package at.netcrawler.cli.agent;

import java.io.Reader;
import java.io.Writer;

import at.andiwand.library.io.CloseableReader;
import at.andiwand.library.io.CloseableWriter;


public abstract class CLIProcessTerminator extends CLISocketHook {
	
	private CloseableReader inCloser;
	private CloseableWriter outCloser;
	
	private final Object lock = new Object();
	private boolean terminated;
	
	public boolean isTerminated() {
		return terminated;
	}
	
	@Override
	public final CLISocket hookSocket(CLISocket socket) {
		inCloser = new CloseableReader(socket.getReader());
		outCloser = new CloseableWriter(socket.getWriter());
		
		Reader reader = inCloser;
		reader = hookReader(reader);
		
		Writer writer = outCloser;
		writer = hookWriter(writer);
		
		return new CLISocket(reader, writer);
	}
	
	@Override
	protected abstract Reader hookReader(Reader reader);
	
	@Override
	protected Writer hookWriter(Writer writer) {
		return writer;
	}
	
	protected final void terminate() {
		inCloser.close();
		outCloser.close();
		
		synchronized (lock) {
			terminated = true;
			lock.notifyAll();
		}
	}
	
	public final void waitFor() throws InterruptedException {
		synchronized (lock) {
			if (terminated) return;
			lock.wait();
		}
	}
	
}