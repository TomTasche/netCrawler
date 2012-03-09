package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.andiwand.library.cli.CommandLineInterface;


public class CommandLineProcess implements CommandLineInterface {
	
	private boolean closed;
	
	private final InputStream in;
	private final OutputStream out;
	
	public CommandLineProcess(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		if (closed) throw new IOException("Stream already closed");
		return in;
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		if (closed) throw new IOException("Stream already closed");
		return out;
	}
	
	@Override
	public void close() throws IOException {
		if (closed) return;
		
		closed = true;
		
		in.close();
		out.close();
	}
	
}