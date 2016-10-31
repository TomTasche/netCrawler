package at.netcrawler.cli.agent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import at.andiwand.library.cli.CommandLineExecutor;
import at.andiwand.library.cli.CommandLineInterface;
import at.andiwand.library.io.FluidInputStreamReader;
import at.andiwand.library.io.StreamUtil;


// TODO: GenericCommandLineAgent
public abstract class CommandLineAgent implements CommandLineExecutor {
	
	protected final InputStream in;
	protected final OutputStream out;
	
	protected final Charset charset;
	
	protected final Reader reader;
	protected final Writer writer;
	
	public CommandLineAgent(CommandLineInterface cli,
			CommandLineAgentSettings settings) throws IOException {
		this.in = getFilterInputStream(cli.getInputStream(), settings);
		this.out = getFilterOutputStream(cli.getOutputStream(), settings);
		
		this.charset = settings.getCharset();
		
		this.reader = getFilterReader(new FluidInputStreamReader(in, charset),
				settings);
		this.writer = getFilterWriter(new OutputStreamWriter(out, charset),
				settings);
	}
	
	protected InputStream getFilterInputStream(InputStream in,
			CommandLineAgentSettings settings) {
		return in;
	}
	
	protected OutputStream getFilterOutputStream(OutputStream out,
			CommandLineAgentSettings settings) {
		return out;
	}
	
	protected Reader getFilterReader(Reader reader,
			CommandLineAgentSettings settings) {
		return reader;
	}
	
	protected Writer getFilterWriter(Writer writer,
			CommandLineAgentSettings settings) {
		return writer;
	}
	
	public abstract CommandLineInterface execute(String command)
			throws IOException;
	
	public String executeAndRead(String command) throws IOException {
		CommandLineInterface process = execute(command);
		InputStream in = process.getInputStream();
		return StreamUtil.readAsString(in, charset);
	}
	
}