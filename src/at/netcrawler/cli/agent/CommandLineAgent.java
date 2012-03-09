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


public abstract class CommandLineAgent implements CommandLineExecutor {
	
	protected final InputStream in;
	protected final OutputStream out;
	
	protected final Charset charset;
	
	protected final Reader reader;
	protected final Writer writer;
	
	public CommandLineAgent(CommandLineInterface cli,
			CommandLineAgentSettings settings) throws IOException {
		this.in = getFilterInputStream(cli.getInputStream());
		this.out = getFilterOutputStream(cli.getOutputStream());
		this.charset = settings.getCharset();
		this.reader = getFilterReader(new FluidInputStreamReader(in, charset));
		this.writer = getFilterWriter(new OutputStreamWriter(out, charset));
	}
	
	protected abstract InputStream getFilterInputStream(InputStream in);
	
	protected abstract OutputStream getFilterOutputStream(OutputStream out);
	
	protected abstract Reader getFilterReader(Reader reader);
	
	protected abstract Writer getFilterWriter(Writer writer);
	
	@Override
	public abstract CommandLineInterface execute(String command)
			throws IOException;
	
	public String executeAndRead(String command) throws IOException {
		CommandLineInterface process = execute(command);
		InputStream in = process.getInputStream();
		return StreamUtil.readAsString(
				in, charset);
	}
	
}