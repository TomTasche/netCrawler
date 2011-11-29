package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import at.andiwand.library.util.StreamUtil;


public class CommandLineProcess {
	
	public static final String DEFAULT_CHARSET = "US-ASCII";
	
	private final InputStream inputStream;
	private final OutputStream outputStream;
	private final Charset charset;
	protected int status;
	
	public CommandLineProcess(InputStream inputStream, OutputStream outputStream) {
		this(inputStream, outputStream, DEFAULT_CHARSET);
	}
	
	public CommandLineProcess(InputStream inputStream,
			OutputStream outputStream, Charset charset) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.charset = charset;
	}
	
	public CommandLineProcess(InputStream inputStream,
			OutputStream outputStream, String charset) {
		this(inputStream, outputStream, Charset.forName(charset));
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	public Charset getCharset() {
		return charset;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String readInput() throws IOException {
		return StreamUtil.readStream(inputStream, charset);
	}
	
}