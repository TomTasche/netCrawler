package at.rennweg.htl.netcrawler.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.andiwand.library.util.cli.Command;


public class CiscoCommand extends Command {
	
	public static final byte[] EXIT_SEQUENCE = {10};
	
	
	private InputStream inputStream;
	private OutputStream outputStream;
	
	
	public CiscoCommand(InputStream inputStream, OutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}
	
	
	public InputStream getErrorStream() {
		return null;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	public int waitFor() throws InterruptedException {
		try {
			while (inputStream.read() != -1);
		} catch (IOException e) {}
		
		return exitValue();
	}
	
	public int exitValue() {
		return 0;
	}
	public void destroy() {
		try {
			outputStream.write(EXIT_SEQUENCE);
			
			waitFor();
		} catch (IOException e) {
		} catch (InterruptedException e) {}
	}
	
}