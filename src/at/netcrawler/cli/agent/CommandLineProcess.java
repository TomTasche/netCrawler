package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.netcrawler.util.StreamUtil;


public class CommandLineProcess {
	
	private final InputStream inputStream;
	private final OutputStream outputStream;
	protected int status;
	
	public CommandLineProcess(InputStream inputStream, OutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	public OutputStream getOutputStream() {
		return outputStream;
	}
	public int getStatus() {
		return status;
	}
	
	public String readInput() throws IOException {
		return StreamUtil.readStream(inputStream);
	}
	
}