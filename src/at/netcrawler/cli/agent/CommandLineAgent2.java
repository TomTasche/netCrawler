package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import at.andiwand.library.cli.CommandLine;
import at.andiwand.library.io.FluidInputStreamReader;


public abstract class CommandLineAgent2 {
	
	public static final Charset DEFAULT_CHARSET = Charset.forName("US-ASCII");
	
	protected final CommandLine commandLine;
	protected final Reader in;
	protected final Writer out;
	
	public CommandLineAgent2(CommandLine commandLine) {
		this(commandLine, DEFAULT_CHARSET);
	}
	
	public CommandLineAgent2(CommandLine commandLine, Charset charset) {
		this.commandLine = commandLine;
		
		try {
			in = initReader(commandLine.getInputStream(), charset);
			out = initWriter(commandLine.getOutputStream(), charset);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected Reader initReader(InputStream in, Charset charset) {
		in = hookInputStream(in);
		return new FluidInputStreamReader(in, charset);
	}
	
	protected Writer initWriter(OutputStream out, Charset charset) {
		out = hookOutputStream(out);
		return new OutputStreamWriter(out, charset);
	}
	
	protected InputStream hookInputStream(InputStream in) {
		return in;
	}
	
	protected OutputStream hookOutputStream(OutputStream out) {
		return out;
	}
	
}