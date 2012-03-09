package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.andiwand.library.cli.CommandLineInterface;
import at.andiwand.library.cli.FilterCommandLineInterface;
import at.andiwand.library.io.CloseableInputStream;
import at.andiwand.library.io.CloseableOutputStream;


public abstract class ProcessFilterCommandLineInterface extends
		FilterCommandLineInterface {
	
	private CloseableInputStream inCloser;
	private CloseableOutputStream outCloser;
	
	private final Object lock = new Object();
	private boolean terminated;
	
	public ProcessFilterCommandLineInterface(CommandLineInterface src) {
		super(src);
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return inCloser;
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		return outCloser;
	}
	
	public boolean isTerminated() {
		return terminated;
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